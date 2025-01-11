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

package cn.enaium.humbleskin.server.error

import org.springframework.http.HttpStatus

/**
 * @author Enaium
 */
data class YggdrasilError(
    val error: String,
    val errorMessage: String,
    val httpStatus: HttpStatus
) : RuntimeException(errorMessage) {

    companion object {
        fun invalidToken() = YggdrasilError("InvalidToken", "Invalid token.", HttpStatus.FORBIDDEN)
        fun invalidCredentials() =
            YggdrasilError("InvalidCredentials", "Invalid credentials. Invalid username or password.", HttpStatus.FORBIDDEN)

        fun accessTokenHasProfile() =
            YggdrasilError("AccessTokenHasProfile", "Access token already has a profile assigned.", HttpStatus.BAD_REQUEST)
    }
}