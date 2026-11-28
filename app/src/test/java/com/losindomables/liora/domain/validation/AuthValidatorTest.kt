package com.losindomables.liora.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `validateName returns error when empty or whitespace`() {
        val result = AuthValidator.validateName("   ")
        assertEquals("El nombre es requerido.", result?.message)
    }

    @Test
    fun `validateName returns null when valid`() {
        val result = AuthValidator.validateName("Vanesa Honorato")
        assertNull(result)
    }

    @Test
    fun `validateEmail returns error when invalid format`() {
        val result = AuthValidator.validateEmail("correo-invalido")
        assertEquals("El formato del correo electrónico no es válido.", result?.message)
    }

    @Test
    fun `validateEmail returns null when valid`() {
        val result = AuthValidator.validateEmail("  TEST@Liora.com  ")
        assertNull(result)
    }

    @Test
    fun `normalizeEmail trims and converts to lowercase`() {
        val normalized = AuthValidator.normalizeEmail("  USER@DOMAIN.COM  ")
        assertEquals("user@domain.com", normalized)
    }

    @Test
    fun `validatePassword returns error when shorter than 8 chars`() {
        val result = AuthValidator.validatePassword("1234567")
        assertEquals("La contraseña debe tener al menos 8 caracteres.", result?.message)
    }

    @Test
    fun `validatePassword returns null when 8 chars or more`() {
        val result = AuthValidator.validatePassword("12345678")
        assertNull(result)
    }
}
