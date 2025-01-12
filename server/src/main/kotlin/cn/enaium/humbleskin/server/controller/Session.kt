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

import cn.enaium.humbleskin.server.error.SessionException
import cn.enaium.humbleskin.server.model.entity.dto.LoginInput
import cn.enaium.humbleskin.server.model.entity.dto.SessionView
import cn.enaium.humbleskin.server.service.SessionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/session")
class Session(
    val sessionService: SessionService
) {
    @PostMapping
    @Throws(SessionException::class)
    fun login(@RequestBody @Valid loginInput: LoginInput): SessionView {
        return sessionService.login(loginInput)
    }

    @DeleteMapping
    fun logout() {
        sessionService.logout()
    }
}