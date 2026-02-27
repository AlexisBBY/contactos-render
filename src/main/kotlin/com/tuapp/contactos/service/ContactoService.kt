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
        page: Int
    ): Page<Contacto> {
        val pageable = PageRequest.of(page.coerceAtLeast(0), 5, Sort.by("id").descending())
        return repo.buscar(nombre.trim(), correo.trim(), telefono.trim(), cp.trim(), fecha, pageable)
    }

    fun guardar(contacto: Contacto, foto: MultipartFile?) {
        // ✅ Si viene foto, la guardamos; si no, NO tronamos
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