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

    companion object {
        private const val MAX_FILE_SIZE = 2 * 1024 * 1024
        private val ALLOWED_IMAGE_TYPES = setOf("image/jpeg", "image/png", "image/webp")
    }

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
            norm(correo)?.lowercase(),
            norm(telefono),
            norm(cp),
            fecha,
            pageable
        )
    }

    fun guardar(contacto: Contacto, foto: MultipartFile?) {
        contacto.nombre = normalizarTexto(contacto.nombre)
        contacto.apellidos = normalizarTexto(contacto.apellidos)
        contacto.correo = contacto.correo.trim().lowercase()
        contacto.telefono = contacto.telefono.trim()
        contacto.cp = contacto.cp.trim()

        validarDuplicadoCorreo(contacto)

        val contactoPersistido = contacto.id?.let { id ->
            repo.findById(id).orElseThrow { IllegalArgumentException("El contacto no existe") }
        }

        contacto.fotoBase64 = when {
            foto != null && !foto.isEmpty -> convertirImagenBase64(foto)
            contactoPersistido != null -> contactoPersistido.fotoBase64
            else -> null
        }

        repo.save(contacto)
    }

    fun obtener(id: Long): Contacto =
        repo.findById(id).orElseThrow { IllegalArgumentException("Contacto no encontrado: $id") }

    fun eliminar(id: Long) {
        if (!repo.existsById(id)) {
            throw IllegalArgumentException("El contacto no existe")
        }
        repo.deleteById(id)
    }

    private fun validarDuplicadoCorreo(contacto: Contacto) {
        val duplicado = if (contacto.id == null) {
            repo.existsByCorreoIgnoreCase(contacto.correo)
        } else {
            repo.existsByCorreoIgnoreCaseAndIdNot(contacto.correo, contacto.id!!)
        }

        if (duplicado) {
            throw IllegalArgumentException("Ya existe un contacto registrado con ese correo")
        }
    }

    private fun convertirImagenBase64(foto: MultipartFile): String {
        if (foto.size > MAX_FILE_SIZE) {
            throw IllegalArgumentException("La imagen no puede superar 2 MB")
        }

        val contentType = foto.contentType?.lowercase().orEmpty()
        if (contentType !in ALLOWED_IMAGE_TYPES) {
            throw IllegalArgumentException("Solo se permiten imágenes JPG, PNG o WEBP")
        }

        return Base64.getEncoder().encodeToString(foto.bytes)
    }

    private fun normalizarTexto(valor: String): String {
        return valor
            .trim()
            .replace(Regex("\\s+"), " ")
    }
}
