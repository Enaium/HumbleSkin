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

import cn.enaium.humbleskin.server.error.AccountException
import cn.enaium.humbleskin.server.model.entity.dto.AccountView
import cn.enaium.humbleskin.server.model.entity.dto.RegisterInput
import cn.enaium.humbleskin.server.service.AccountService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author Enaium
 */
@Validated
@RestController
@RequestMapping("/account")
class Account(
    private val accountService: AccountService
) {
    @PostMapping("/register")
    @Throws(AccountException::class)
    fun register(@RequestBody @Valid registerInput: RegisterInput) {
        accountService.register(registerInput)
    }

    @GetMapping
    fun get(): AccountView {
        return accountService.get()
    }
}