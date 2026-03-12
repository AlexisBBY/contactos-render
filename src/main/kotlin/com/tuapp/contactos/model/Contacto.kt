package com.tuapp.contactos.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "contactos")
data class Contacto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val cp: String,
    val fechaNacimiento: LocalDate?,
    var fotoActualBase64: String? = null
)