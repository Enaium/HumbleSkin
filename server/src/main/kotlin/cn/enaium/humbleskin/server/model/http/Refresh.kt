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

package cn.enaium.humbleskin.server.model.http

import cn.enaium.humbleskin.server.model.converter.UnsignedUuidDeserializer
import cn.enaium.humbleskin.server.model.converter.UnsignedUuidSerializer
import cn.enaium.humbleskin.server.model.entity.Account
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * @author Enaium
 */
data class RefreshRequest(
    @JsonDeserialize(using = UnsignedUuidDeserializer::class)
    val accessToken: UUID,
    @JsonDeserialize(using = UnsignedUuidDeserializer::class)
    val clientToken: UUID?,
    val requestUser: Boolean = false,
    val selectedProfile: Profile?
) {
    data class Profile(
        @JsonDeserialize(using = UnsignedUuidDeserializer::class)
        val id: UUID,
        val name: String
    )
}

data class RefreshResponse(
    @JsonSerialize(using = UnsignedUuidSerializer::class)
    val accessToken: UUID,
    @JsonSerialize(using = UnsignedUuidSerializer::class)
    val clientToken: UUID,
    val selectedProfile: Profile? = null,
    val account: Account? = null
) {
    data class Profile(
        @JsonSerialize(using = UnsignedUuidSerializer::class)
        val id: UUID,
        val name: String
    )
}