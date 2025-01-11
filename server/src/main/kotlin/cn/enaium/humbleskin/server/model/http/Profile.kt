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

import cn.enaium.humbleskin.server.model.converter.Base64EncodeSerializer
import cn.enaium.humbleskin.server.model.converter.UnsignedUuidDeserializer
import cn.enaium.humbleskin.server.model.converter.UnsignedUuidSerializer
import cn.enaium.humbleskin.server.model.entity.TextureType
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * @author Enaium
 */
data class ProfileRequest(
    val uuid: UUID
)

data class ProfileResponse(
    val properties: List<Property>,
    @JsonSerialize(using = UnsignedUuidSerializer::class)
    val id: UUID,
    val name: String
) {
    data class Property(
        val name: String = "textures",
        @JsonSerialize(using = Base64EncodeSerializer::class)
        val value: Textures,
    ) {
        data class Textures(
            val timestamp: Long,
            val textures: Map<TextureType, Texture>,
            @JsonSerialize(using = UnsignedUuidSerializer::class)
            val profileId: UUID,
            val profileName: String
        ) {
            data class Texture(
                val url: String,
                val metadata: Metadata?
            ) {
                data class Metadata(
                    val model: String
                )
            }
        }
    }
}