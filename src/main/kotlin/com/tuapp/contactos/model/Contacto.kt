package com.tuapp.contactos.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(name = "contactos")
data class Contacto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @field:NotBlank(message = "Nombre es obligatorio")
    @Column(nullable = false)
    var nombre: String = "",

    @field:NotBlank(message = "Apellidos es obligatorio")
    @Column(nullable = false)
    var apellidos: String = "",

    @field:NotBlank(message = "Correo es obligatorio")
    @field:Email(message = "Correo inválido")
    @Column(nullable = false)
    var correo: String = "",

    @field:NotBlank(message = "Teléfono es obligatorio")
    @field:Pattern(regexp = "^[0-9]{10}$", message = "Teléfono debe tener 10 dígitos")
    @Column(nullable = false, length = 10)
    var telefono: String = "",

    @field:NotBlank(message = "Código Postal es obligatorio")
    @field:Pattern(regexp = "^[0-9]{5}$", message = "CP debe tener 5 dígitos")
    @Column(nullable = false, length = 5)
    var cp: String = "",

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var fechaNacimiento: LocalDate? = null,

    @Lob
    @Column(columnDefinition = "TEXT")
    var fotoBase64: String? = null
)