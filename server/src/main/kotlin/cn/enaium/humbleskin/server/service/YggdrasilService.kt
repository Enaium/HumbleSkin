/*
 * Humble Skin is a humble Minecraft skin hosting server.
 *
 * Copyright (C) 2025  Enaium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cn.enaium.humbleskin.server.service

import cn.enaium.humbleskin.server.config.CoreConfig
import cn.enaium.humbleskin.server.error.YggdrasilError
import cn.enaium.humbleskin.server.model.entity.*
import cn.enaium.humbleskin.server.model.entity.dto.LoginView
import cn.enaium.humbleskin.server.model.entity.dto.LoginWithCharacterNameView
import cn.enaium.humbleskin.server.model.entity.dto.ProfileView
import cn.enaium.humbleskin.server.model.http.*
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path

/**
 * @author Enaium
 */
@Service
class YggdrasilService(
    private val sql: KSqlClient,
    private val coreConfig: CoreConfig,
    private val stringRedisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) {

    val timeout = 15L

    val accessTokenKey = "yggdrasil:accessToken"
    val accountKey = "yggdrasil:account"
    val serverKey = "yggdrasil:server"

    fun login(loginRequest: LoginRequest): LoginResponse {

        val account = findAccount(loginRequest.username) ?: throw YggdrasilError.invalidCredentials()

        if (DigestUtils.md5DigestAsHex(loginRequest.password.toByteArray()) != account.password) {
            throw YggdrasilError.invalidCredentials()
        }

        val accountInfo = issueToken(account.id, loginRequest.clientToken)

        return LoginResponse(
            accountInfo.accessToken,
            accountInfo.clientToken,
            account.characters.map {
                LoginResponse.Profile(it.id, it.name)
            },
            account.characters.firstOrNull()?.let {
                LoginResponse.Profile(it.id, it.name)
            },
            if (loginRequest.requestUser) {
                LoginResponse.User(account.id)
            } else {
                null
            }
        )
    }

    private fun findAccount(username: String): LoginView? {
        var character: LoginWithCharacterNameView? = null

        if (coreConfig.loginWithCharacterName) {
            character = sql.createQuery(Character::class) {
                where(table.name eq username)
                select(table.fetch(LoginWithCharacterNameView::class))
            }.fetchOneOrNull()
        }

        val account = sql.createQuery(Account::class) {
            where(table.email eq (character?.account?.email ?: username))
            select(table.fetch(LoginView::class))
        }.fetchOneOrNull()
        return account
    }

    fun profile(profile: ProfileRequest): ProfileResponse {
        val character = sql.createQuery(Character::class) {
            where(table.id eq profile.uuid)
            select(table.fetch(ProfileView::class))
        }.fetchOneOrNull() ?: throw YggdrasilError.invalidToken()

        return ProfileResponse(
            listOf(
                ProfileResponse.Property(
                    value = ProfileResponse.Property.Textures(
                        System.currentTimeMillis(),
                        character.textures.associate {
                            it.type to ProfileResponse.Property.Textures.Texture(
                                "${coreConfig.url}/textures/${it.hash}",
                                if (it.type == TextureType.SKIN) {
                                    ProfileResponse.Property.Textures.Texture.Metadata(character.model.modelName)
                                } else {
                                    null
                                }
                            )
                        },
                        character.id,
                        character.name
                    )
                )
            ),
            character.id,
            character.name
        )
    }

    fun textures(hash: String): ResponseEntity<ByteArray> {
        return Path("textures").resolve("$hash.png").takeIf { it.toFile().exists() }?.let { file ->
            ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(file.toFile().readBytes())
        } ?: ResponseEntity.noContent().build()
    }

    fun refresh(refreshRequest: RefreshRequest): RefreshResponse {
        return authenticate(refreshRequest.accessToken, refreshRequest.clientToken).let {
            RefreshResponse(it.accessToken, it.clientToken)
        }
    }

    fun validate(validateRequest: RefreshRequest) {
        authenticate(validateRequest.accessToken, validateRequest.clientToken)
    }

    fun invalidate(invalidateRequest: InvalidateRequest) {
        destroyToken(invalidateRequest.accessToken)
    }

    fun signout(invalidateRequest: SignoutRequest) {
        val account = findAccount(invalidateRequest.username) ?: throw YggdrasilError.invalidCredentials()

        if (DigestUtils.md5DigestAsHex(invalidateRequest.password.toByteArray()) != account.password) {
            throw YggdrasilError.invalidCredentials()
        }

        destroyToken(account.id)
    }

    fun join(joinRequest: JoinRequest, request: HttpServletRequest) {
        val accountInfo = authenticate(joinRequest.accessToken)

        val serverInfo = ServerInfo(
            accountInfo,
            joinRequest.serverId,
            request.remoteAddr
        )

        stringRedisTemplate.opsForValue()
            .set("server:${serverInfo.serverId}", objectMapper.writeValueAsString(serverInfo), timeout, TimeUnit.DAYS)
    }

    fun hasJoined(hasJoinedRequest: HasJoinedRequest): ProfileResponse? {
        val account = findAccount(hasJoinedRequest.username) ?: return null
        val serverInfoString =
            stringRedisTemplate.opsForValue()["$serverKey:${hasJoinedRequest.serverId}"] ?: return null
        val serverInfo = objectMapper.readValue(serverInfoString, ServerInfo::class.java)

        if (serverInfo.accountInfo.id != account.id) {
            return null
        }

        return profile(ProfileRequest(serverInfo.accountInfo.id))
    }

    fun authenticate(accessToken: UUID, clientToken: UUID? = null): AccountInfo {
        val accountInfoString = stringRedisTemplate.opsForValue()["$accessTokenKey:$accessToken"]
            ?: throw YggdrasilError.invalidToken()

        val accountInfo = objectMapper.readValue(accountInfoString, AccountInfo::class.java)
        clientToken?.also {
            if (accountInfo.clientToken != it) {
                throw YggdrasilError.invalidToken()
            }
        }
        stringRedisTemplate.expire("$accountKey:${accountInfo.id}", timeout, TimeUnit.DAYS)
        stringRedisTemplate.expire("$accessTokenKey:$accessToken", timeout, TimeUnit.DAYS)
        return accountInfo
    }

    fun issueToken(
        accountId: UUID,
        clientToken: UUID?
    ): AccountInfo {
        val accountInfo = AccountInfo(
            accountId,
            clientToken ?: UUID.randomUUID(),
            UUID.randomUUID()
        )

        val accountInfoString = objectMapper.writeValueAsString(accountInfo)

        if (stringRedisTemplate.opsForValue()[accountId.toString()] != null) {
            stringRedisTemplate.delete("$accountKey:${accountId}")
            stringRedisTemplate.delete("$accessTokenKey:${accountInfo.accessToken}")
        }

        stringRedisTemplate.opsForValue()
            .set("$accountKey:${accountId}", accountInfoString, timeout, TimeUnit.DAYS)
        stringRedisTemplate.opsForValue()
            .set("$accessTokenKey:${accountInfo.accessToken}", accountInfoString, timeout, TimeUnit.DAYS)
        return accountInfo
    }

    fun destroyToken(accountId: UUID? = null, accessToken: UUID? = null) {
        accountId?.also {
            stringRedisTemplate.opsForValue()["$accountKey:${accountId}"]?.also {
                val accountInfo = objectMapper.readValue(it, AccountInfo::class.java)
                stringRedisTemplate.delete("$accessTokenKey:${accountInfo.accessToken}")
                stringRedisTemplate.delete("$accountKey:${accountInfo.id}")
            }
        }

        accountId?.also {
            stringRedisTemplate.opsForValue()["$accessTokenKey:${accessToken}"]?.also {
                val accountInfo = objectMapper.readValue(it, AccountInfo::class.java)
                stringRedisTemplate.delete("$accountKey:${accountInfo.id}")
                stringRedisTemplate.delete("$accessTokenKey:${accountInfo.accessToken}")
            }
        }
    }

    data class AccountInfo(
        val id: UUID,
        val clientToken: UUID,
        val accessToken: UUID
    )

    data class ServerInfo(
        val accountInfo: AccountInfo,
        val serverId: String,
        val ip: String,
    )
}