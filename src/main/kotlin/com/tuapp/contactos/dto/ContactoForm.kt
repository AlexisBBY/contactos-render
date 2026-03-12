package com.tuapp.contactos.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class ContactoForm(

    var id: Long? = null,

    @field:NotBlank(message = "El nombre es obligatorio.")
    @field:Size(min = 2, max = 60, message = "El nombre debe tener entre 2 y 60 caracteres.")
    @field:Pattern(
        regexp = "^[\\p{L}][\\p{L}\\s'\\-]*$",
        message = "El nombre solo puede contener letras, espacios, apóstrofo y guion."
    )
    var nombre: String = "",

    @field:NotBlank(message = "Los apellidos son obligatorios.")
    @field:Size(min = 2, max = 80, message = "Los apellidos deben tener entre 2 y 80 caracteres.")
    @field:Pattern(
        regexp = "^[\\p{L}][\\p{L}\\s'\\-]*$",
        message = "Los apellidos solo pueden contener letras, espacios, apóstrofo y guion."
    )
    var apellidos: String = "",

    @field:NotBlank(message = "El correo es obligatorio.")
    @field:Email(message = "El correo no es válido.")
    @field:Size(max = 120, message = "El correo no puede exceder 120 caracteres.")
    @field:Pattern(
        regexp = "^[^<>]+$",
        message = "El correo contiene caracteres no permitidos."
    )
    var correo: String = "",

    @field:NotBlank(message = "El teléfono es obligatorio.")
    @field:Pattern(
        regexp = "^[0-9]{10}$",
        message = "El teléfono debe tener exactamente 10 dígitos."
    )
    var telefono: String = "",

    @field:NotBlank(message = "El código postal es obligatorio.")
    @field:Pattern(
        regexp = "^[0-9]{5}$",
        message = "El código postal debe tener exactamente 5 dígitos."
    )
    var cp: String = "",

    @field:PastOrPresent(message = "La fecha de nacimiento no puede ser futura.")
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var fechaNacimiento: LocalDate? = null,

    var fotoActualBase64: String? = null
)