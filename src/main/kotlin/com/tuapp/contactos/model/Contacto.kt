package com.tuapp.contactos.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(
    name = "contactos",
    uniqueConstraints = [UniqueConstraint(name = "uk_contactos_correo", columnNames = ["correo"])]
)
data class Contacto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @field:Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
        message = "El nombre solo puede contener letras, espacios, apóstrofe y guion"
    )
    @Column(nullable = false, length = 80)
    var nombre: String = "",

    @field:NotBlank(message = "Los apellidos son obligatorios")
    @field:Size(min = 2, max = 120, message = "Los apellidos deben tener entre 2 y 120 caracteres")
    @field:Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
        message = "Los apellidos solo pueden contener letras, espacios, apóstrofe y guion"
    )
    @Column(nullable = false, length = 120)
    var apellidos: String = "",

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Size(max = 120, message = "El correo no puede exceder 120 caracteres")
    @field:Email(message = "El correo no es válido")
    @Column(nullable = false, length = 120)
    var correo: String = "",

    @field:NotBlank(message = "El teléfono es obligatorio")
    @field:Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
    @Column(nullable = false, length = 10)
    var telefono: String = "",

    @field:NotBlank(message = "El código postal es obligatorio")
    @field:Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener exactamente 5 dígitos")
    @Column(nullable = false, length = 5)
    var cp: String = "",

    @field:Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var fechaNacimiento: LocalDate? = null,

    @Lob
    @Column(columnDefinition = "TEXT")
    var fotoBase64: String? = null
)
