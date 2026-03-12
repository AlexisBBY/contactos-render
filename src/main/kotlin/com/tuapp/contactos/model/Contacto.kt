package com.tuapp.contactos.model

<<<<<<< HEAD
import jakarta.persistence.*
=======
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
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
import java.time.LocalDate

@Entity
@Table(
    name = "contactos",
<<<<<<< HEAD
    indexes = [
        Index(name = "idx_contactos_correo", columnList = "correo"),
        Index(name = "idx_contactos_telefono", columnList = "telefono"),
        Index(name = "idx_contactos_cp", columnList = "cp")
    ]
)
class Contacto(
=======
    uniqueConstraints = [UniqueConstraint(name = "uk_contactos_correo", columnNames = ["correo"])]
)
data class Contacto(
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

<<<<<<< HEAD
    @Column(nullable = false, length = 60)
    var nombre: String = "",

    @Column(nullable = false, length = 80)
    var apellidos: String = "",

    @Column(nullable = false, length = 120)
    var correo: String = "",

    @Column(nullable = false, length = 10)
    var telefono: String = "",

    @Column(nullable = false, length = 5)
    var cp: String = "",

    @Column(name = "fecha_nacimiento")
=======
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
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
    var fechaNacimiento: LocalDate? = null,

    @Lob
    @Column(name = "foto_base64", columnDefinition = "TEXT")
    var fotoBase64: String? = null
)
