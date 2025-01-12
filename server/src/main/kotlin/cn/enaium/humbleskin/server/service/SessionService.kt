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

import cn.enaium.humbleskin.server.error.SessionException
import cn.enaium.humbleskin.server.model.entity.Account
import cn.enaium.humbleskin.server.model.entity.dto.LoginInput
import cn.enaium.humbleskin.server.model.entity.dto.SessionView
import cn.enaium.humbleskin.server.model.entity.email
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils

@Service
class SessionService(
    val sql: KSqlClient,
    val authService: AuthService
) {
    fun login(loginInput: LoginInput): SessionView {
        val account = findAccount(loginInput)
        if (DigestUtils.md5DigestAsHex(loginInput.password.toByteArray()) != account.password) {
            throw SessionException.usernameOrPasswordError()
        }
        return SessionView(account.id, authService.issue(account))
    }

    fun findAccount(loginInput: LoginInput): Account {
        return sql.createQuery(Account::class) {
            where(table.email eq loginInput.email)
            select(table)
        }.fetchOneOrNull() ?: throw SessionException.usernameOrPasswordError()
    }

    fun logout() {
        authService.destroy()
    }
}
