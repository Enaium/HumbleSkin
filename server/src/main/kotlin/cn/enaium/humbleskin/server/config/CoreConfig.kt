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

import cn.enaium.humbleskin.server.model.ServerMeta
import cn.enaium.humbleskin.server.utility.Key
import cn.enaium.humbleskin.server.utility.Key.toPem
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * @author Enaium
 */
@ConfigurationProperties(prefix = "humbleskin.core")
data class CoreConfig(
    val skinDomains: List<String> = listOf("localhost"),
    val url: String,
    val serverName: String = "Humble Skin",
    val loginWithCharacterName: Boolean = true
) {

    @Bean
    fun publicKeyPem(): String {
        return Key.keyPair.public.toPem()
    }

    @Bean
    fun serverMeta(
        @Value("#{publicKeyPem}") publicKeyPem: String
    ): ServerMeta {
        return ServerMeta(
            skinDomains,
            publicKeyPem,
            mapOf("serverName" to serverName, "feature.non_email_login" to loginWithCharacterName)
        )
    }
}