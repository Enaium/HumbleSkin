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

package cn.enaium.humbleskin.server.controller

import cn.enaium.humbleskin.server.error.YggdrasilError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * @author Enaium
 */
@ControllerAdvice
class Error {
    @ExceptionHandler(YggdrasilError::class)
    fun yggdrasilError(e: YggdrasilError): ResponseEntity<YggdrasilError> {
        return ResponseEntity.status(e.httpStatus).body(e)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity.badRequest().build()
    }
}