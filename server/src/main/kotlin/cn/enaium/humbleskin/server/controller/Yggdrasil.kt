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

package cn.enaium.humbleskin.server.controller

import cn.enaium.humbleskin.server.model.ServerMeta
import cn.enaium.humbleskin.server.model.http.*
import cn.enaium.humbleskin.server.service.YggdrasilService
import cn.enaium.humbleskin.server.utility.toUUID
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @author Enaium
 */
@RestController
class Yggdrasil(
    private val serverMeta: ServerMeta,
    private val yggdrasilService: YggdrasilService
) {
    @GetMapping("/")
    fun meta(): ServerMeta {
        return serverMeta
    }

    @PostMapping("/authserver/authenticate")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        return yggdrasilService.login(loginRequest)
    }

    @PostMapping("/authserver/refresh")
    fun refresh(@RequestBody refreshRequest: RefreshRequest): RefreshResponse {
        return yggdrasilService.refresh(refreshRequest)
    }

    @PostMapping("/authserver/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun validate(@RequestBody validateRequest: RefreshRequest) {
        yggdrasilService.validate(validateRequest)
    }

    @PostMapping("/authserver/invalidate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun invalidate(@RequestBody invalidateRequest: InvalidateRequest) {
        yggdrasilService.invalidate(invalidateRequest)
    }

    @PostMapping("/authserver/signout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun signout(@RequestBody signoutRequest: SignoutRequest) {
        yggdrasilService.signout(signoutRequest)
    }

    @PostMapping("/sessionserver/session/minecraft/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun join(@RequestBody joinRequest: JoinRequest, request: HttpServletRequest) {
        yggdrasilService.join(joinRequest, request)
    }

    @GetMapping("/sessionserver/session/minecraft/hasJoined")
    fun hasJoined(@RequestParam username: String, @RequestParam serverId: String): ProfileResponse? {
        return yggdrasilService.hasJoined(HasJoinedRequest(username, serverId))
    }

    @GetMapping("/sessionserver/session/minecraft/profile/{uuid:[a-f0-9]{32}}")
    fun profile(@PathVariable uuid: String): ProfileResponse {
        return yggdrasilService.profile(ProfileRequest(uuid.toUUID()))
    }

    @GetMapping("/textures/{hash:[a-f0-9]{64}}")
    fun texture(@PathVariable hash: String): ResponseEntity<ByteArray> {
        return yggdrasilService.textures(hash)
    }
}