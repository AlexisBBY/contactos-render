package com.tuapp.contactos.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "contactos",
    indexes = [
        Index(name = "idx_contactos_correo", columnList = "correo"),
        Index(name = "idx_contactos_telefono", columnList = "telefono"),
        Index(name = "idx_contactos_cp", columnList = "cp")
    ]
)
class Contacto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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
    var fechaNacimiento: LocalDate? = null,

    @Lob
    @Column(name = "foto_base64", columnDefinition = "TEXT")
    var fotoBase64: String? = null
)