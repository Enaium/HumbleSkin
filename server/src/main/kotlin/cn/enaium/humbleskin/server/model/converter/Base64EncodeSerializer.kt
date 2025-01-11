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

package cn.enaium.humbleskin.server.model.converter

import cn.enaium.humbleskin.server.model.http.ProfileResponse
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

/**
 * @author Enaium
 */
class Base64EncodeSerializer : JsonSerializer<ProfileResponse.Property.Textures>() {
    override fun serialize(
        value: ProfileResponse.Property.Textures,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeString(Base64.getEncoder().encodeToString(jacksonObjectMapper().writeValueAsBytes(value)))
    }
}