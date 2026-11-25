package com.losindomables.liora.domain.validation

import com.losindomables.liora.domain.model.AuthError

/**
 * Validador de formularios de autenticación según las reglas de negocio y del backend.
 */
object AuthValidator {

    // Regex para validar email sin depender del framework Android util.Patterns en tests JVM
    private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}$".toRegex()

    /**
     * Valida que el nombre no esté vacío.
     */
    fun validateName(name: String): AuthError.ValidationError? {
        if (name.trim().isEmpty()) {
            return AuthError.ValidationError("El nombre es requerido.")
        }
        return null
    }

    /**
     * Valida el formato del correo electrónico.
     */
    fun validateEmail(email: String): AuthError.ValidationError? {
        val normalized = normalizeEmail(email)
        if (normalized.isEmpty()) {
            return AuthError.ValidationError("El correo electrónico es requerido.")
        }
        if (!normalized.matches(EMAIL_REGEX)) {
            return AuthError.ValidationError("El formato del correo electrónico no es válido.")
        }
        return null
    }

    /**
     * Valida que la contraseña tenga al menos 8 caracteres.
     */
    fun validatePassword(password: String): AuthError.ValidationError? {
        if (password.length < 8) {
            return AuthError.ValidationError("La contraseña debe tener al menos 8 caracteres.")
        }
        return null
    }

    /**
     * Normaliza el correo electrónico eliminando espacios de los extremos y convirtiéndolo a minúsculas.
     */
    fun normalizeEmail(email: String): String = email.trim().lowercase()

    /**
     * Valida los campos del formulario de registro.
     */
    fun validateRegister(name: String, email: String, password: String): AuthError.ValidationError? {
        return validateName(name)
            ?: validateEmail(email)
            ?: validatePassword(password)
    }

    /**
     * Valida los campos del formulario de login.
     */
    fun validateLogin(email: String, password: String): AuthError.ValidationError? {
        return validateEmail(email)
            ?: validatePassword(password)
    }
}
