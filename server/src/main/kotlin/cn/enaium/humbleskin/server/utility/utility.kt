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

package cn.enaium.humbleskin.server.utility

import java.util.*

/**
 * @author Enaium
 */
fun UUID.unsigned(): String {
    return this.toString().replace("-", "")
}

fun String.toUUID(): UUID {
    return when (this.length) {
        32 -> UUID.fromString(
            this.substring(0, 8) + "-" + this.substring(8, 12) + "-" + this.substring(
                12,
                16
            ) + "-" + this.substring(16, 20) + "-" + this.substring(20)
        )

        36 -> UUID.fromString(this)
        else -> throw IllegalArgumentException("Invalid UUID: $this")
    }
}