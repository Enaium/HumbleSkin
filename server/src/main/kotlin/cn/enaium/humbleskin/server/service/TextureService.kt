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

import cn.enaium.humbleskin.server.model.entity.Texture
import cn.enaium.humbleskin.server.model.entity.dto.TextureInput
import org.apache.commons.codec.digest.DigestUtils
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.stereotype.Service
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

@Service
class TextureService(
    private val sql: KSqlClient
) {
    fun save(texture: TextureInput) {
        val decode = Base64.getDecoder().decode(texture.content.split(",")[1])
        val sha256Hex = DigestUtils.sha256Hex(decode)
        val path = Path(System.getProperty("user.dir"), "textures", "${sha256Hex}.png")
        if (!path.exists()) {
            path.writeBytes(decode)
            sql.save(Texture {
                characterId = texture.characterId
                type = texture.type
                hash = sha256Hex
            })
        }
    }
}
