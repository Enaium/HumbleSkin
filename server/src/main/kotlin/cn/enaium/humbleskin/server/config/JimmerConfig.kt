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

package cn.enaium.humbleskin.server.config

import cn.enaium.humbleskin.server.model.entity.Character
import cn.enaium.humbleskin.server.model.entity.ModelType
import cn.enaium.humbleskin.server.model.entity.Texture
import cn.enaium.humbleskin.server.model.entity.TextureType
import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.kt.cfg.KCustomizer
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import org.postgresql.util.PGobject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

/**
 * @author Enaium
 */
@Configuration
class JimmerConfig {
    @Bean
    fun customizer(): KCustomizer = KCustomizer {
        it.setScalarProvider(Character::model, EnumScalar(ModelType::class))
        it.setScalarProvider(Texture::type, EnumScalar(TextureType::class))
    }

    private class EnumScalar<E : Enum<E>>(val enumType: KClass<E>) : ScalarProvider<E, PGobject> {
        override fun toScalar(sqlValue: PGobject): E {
            return enumType.java.declaredFields.firstOrNull {
                it.getAnnotation(EnumItem::class.java)?.name == sqlValue.value
            }?.let { java.lang.Enum.valueOf(enumType.java, it.name) } ?: throw IllegalArgumentException()
        }

        override fun toSql(scalarValue: E): PGobject {
            return PGobject().apply {
                type = enumType.simpleName?.toSnakeCase() ?: ""
                value = enumType.java.declaredFields.firstOrNull {
                    it.name == scalarValue.name
                }?.getAnnotation(EnumItem::class.java)?.name ?: ""
            }
        }

        fun String.toSnakeCase(): String {
            return replace(Regex("(?<!^)([A-Z])"), "_$1").lowercase()
        }
    }
}