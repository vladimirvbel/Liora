package com.losindomables.liora.domain.model

/**
 * Roles soportados por la plataforma Liora.
 */
enum class Role {
    CLIENTE,
    VENDEDOR,
    ADMIN;

    companion object {
        fun fromString(value: String?): Role {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: CLIENTE
        }
    }
}
