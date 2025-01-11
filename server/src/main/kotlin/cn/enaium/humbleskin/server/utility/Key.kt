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

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.util.*

/**
 * @author Enaium
 */
object Key {

    val keyPair = generateKey()

    fun generateKey(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }
        return keyPairGenerator.genKeyPair()
    }

    fun PublicKey.toPem(): String {
        var pem = ""
        pem += "-----BEGIN PUBLIC KEY-----\n"
        pem += "${Base64.getMimeEncoder(76, byteArrayOf('\n'.code.toByte())).encodeToString(encoded)}\n"
        pem += "-----END PUBLIC KEY-----\n"
        return pem
    }
}