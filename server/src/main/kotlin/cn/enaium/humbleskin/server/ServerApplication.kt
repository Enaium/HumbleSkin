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

package cn.enaium.humbleskin.server

import cn.enaium.humbleskin.server.config.CoreConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

/**
 * @author Enaium
 */
@SpringBootApplication
@EnableConfigurationProperties(CoreConfig::class)
open class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
