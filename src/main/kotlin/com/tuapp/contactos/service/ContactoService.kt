package com.tuapp.contactos.service

import com.tuapp.contactos.model.Contacto
import com.tuapp.contactos.repository.ContactoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.util.Base64

@Service
class ContactoService(private val repo: ContactoRepository) {

    fun buscar(
        nombre: String,
        correo: String,
        telefono: String,
        cp: String,
        fecha: LocalDate?,
        page: Int,
        size: Int
    ): Page<Contacto> {

        fun norm(s: String): String? = s.trim().takeIf { it.isNotEmpty() }

        val pageable = PageRequest.of(
            page.coerceAtLeast(0),
            size.coerceIn(1, 50),
            Sort.by(Sort.Direction.DESC, "id")
        )

        return repo.buscar(
            norm(nombre),
            norm(correo),
            norm(telefono),
            norm(cp),
            fecha,
            pageable
        )
    }

    fun guardar(contacto: Contacto, foto: MultipartFile?) {
        if (foto != null && !foto.isEmpty) {
            contacto.fotoBase64 = Base64.getEncoder().encodeToString(foto.bytes)
        }
        repo.save(contacto)
    }

    fun obtener(id: Long): Contacto =
        repo.findById(id).orElseThrow { RuntimeException("Contacto no encontrado: $id") }

    fun eliminar(id: Long) {
        repo.deleteById(id)
    }
}