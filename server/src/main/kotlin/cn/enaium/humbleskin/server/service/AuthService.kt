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

import cn.enaium.humbleskin.server.model.entity.Account
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Enaium
 */
@Service
class AuthService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val httpServletRequest: HttpServletRequest,
    private val objectMapper: ObjectMapper
) {

    val authAuthorization = "auth:authorization"
    val authAccount = "auth:account"

    fun issue(account: Account): String {
        stringRedisTemplate.opsForValue()["$authAccount:${account.id}"]?.also {
            return it
        }

        val authorization = UUID.randomUUID().toString()

        stringRedisTemplate.opsForValue()["$authAuthorization:$authorization"] =
            objectMapper.writeValueAsString(account)
        stringRedisTemplate.opsForValue()["$authAccount:${account.id}"] = authorization

        return authorization
    }

    fun auth(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorization.isNullOrBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return false
        }

        val hasKey = stringRedisTemplate.hasKey("$authAuthorization:$authorization")

        if (!hasKey) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }

        return hasKey
    }

    fun auth(accountId: UUID): Boolean {
        val authAuthorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)

        if (authAuthorization.isNullOrBlank()) {
            return false
        }

        stringRedisTemplate.opsForValue().get("$authAuthorization:$authAuthorization")?.also {
            return objectMapper.readValue(it, Account::class.java).id == accountId
        }

        return false
    }

    fun getAccountId(): UUID {
        val authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorization.isNullOrBlank()) {
            throw Exception("Impossible")
        }

        stringRedisTemplate.opsForValue().get("$authAuthorization:$authorization")?.also {
            return objectMapper.readValue(it, Account::class.java).id
        }

        throw Exception("Impossible")
    }

    fun destroy() {
        val authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)
        stringRedisTemplate.opsForValue()["$authAuthorization:$authorization"]?.also {
            stringRedisTemplate.delete("$authAccount:${objectMapper.readValue(it, Account::class.java).id}")
            stringRedisTemplate.delete("$authAuthorization:$authorization")
        }
    }
}