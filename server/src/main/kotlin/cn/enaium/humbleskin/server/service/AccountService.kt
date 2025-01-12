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

import cn.enaium.humbleskin.server.error.AccountException
import cn.enaium.humbleskin.server.model.entity.Account
import cn.enaium.humbleskin.server.model.entity.dto.AccountView
import cn.enaium.humbleskin.server.model.entity.dto.RegisterInput
import cn.enaium.humbleskin.server.model.entity.email
import cn.enaium.humbleskin.server.model.entity.id
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils

@Service
class AccountService(
    val sql: KSqlClient,
    val authService: AuthService
) {
    fun register(registerInput: RegisterInput) {
        if (accountExists(registerInput)) {
            throw AccountException.emailExist()
        }

        if (registerInput.password != registerInput.confirmPassword) {
            throw AccountException.confirmPasswordError()
        }

        sql.save(
            registerInput.copy(password = DigestUtils.md5DigestAsHex(registerInput.password.toByteArray())).toEntity()
        ) {
            setMode(SaveMode.INSERT_IF_ABSENT)
        }
    }

    fun accountExists(registerInput: RegisterInput): Boolean {
        return sql.createQuery(Account::class) {
            where(table.email eq registerInput.email)
            select(table)
        }.fetchOneOrNull() != null
    }

    fun get(): AccountView {
        return sql.createQuery(Account::class) {
            where(table.id eq authService.getAccountId())
            select(table.fetch(AccountView::class))
        }.fetchOne()
    }
}
