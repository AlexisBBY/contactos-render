package com.tuapp.contactos.service

import com.tuapp.contactos.dto.ContactoForm
import com.tuapp.contactos.model.Contacto
import com.tuapp.contactos.repository.ContactoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

        val pageable = PageRequest.of(
            page.coerceAtLeast(0),
            size.coerceIn(1, 50),
            Sort.by(Sort.Direction.DESC, "id")
        )

        return repo.buscar(
<<<<<<< HEAD
            sanitizarBusqueda(nombre),
            sanitizarBusqueda(correo),
            sanitizarBusqueda(telefono),
            sanitizarBusqueda(cp),
=======
            norm(nombre),
            norm(correo)?.lowercase(),
            norm(telefono),
            norm(cp),
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
            fecha,
            pageable
        )
    }

<<<<<<< HEAD
    @Transactional
    fun guardar(form: ContactoForm, foto: MultipartFile?): Contacto {
        val contacto = if (form.id != null) {
            repo.findById(form.id!!)
                .orElseThrow { IllegalArgumentException("El contacto que intentas editar no existe.") }
        } else {
            Contacto()
        }

        contacto.nombre = normalizarTexto(form.nombre, 60, "Nombre")
        contacto.apellidos = normalizarTexto(form.apellidos, 80, "Apellidos")
        contacto.correo = normalizarCorreo(form.correo)
        contacto.telefono = normalizarDigitos(form.telefono, 10, "Teléfono")
        contacto.cp = normalizarDigitos(form.cp, 5, "Código postal")

        if (form.fechaNacimiento != null && form.fechaNacimiento!!.isAfter(LocalDate.now())) {
            throw IllegalArgumentException("La fecha de nacimiento no puede ser futura.")
        }
        contacto.fechaNacimiento = form.fechaNacimiento

        if (foto != null && !foto.isEmpty) {
            validarFoto(foto)
            contacto.fotoBase64 = Base64.getEncoder().encodeToString(foto.bytes)
        }

        return repo.save(contacto)
    }

    @Transactional(readOnly = true)
    fun obtenerFormulario(id: Long): ContactoForm {
        val contacto = obtener(id)
        return ContactoForm(
            id = contacto.id,
            nombre = contacto.nombre,
            apellidos = contacto.apellidos,
            correo = contacto.correo,
            telefono = contacto.telefono,
            cp = contacto.cp,
            fechaNacimiento = contacto.fechaNacimiento,
            fotoActualBase64 = contacto.fotoBase64
        )
    }
=======
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
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd

    @Transactional(readOnly = true)
    fun obtenerFotoBase64(id: Long): String? {
        return obtener(id).fotoBase64
    }

    @Transactional(readOnly = true)
    fun obtener(id: Long): Contacto {
        return repo.findById(id)
            .orElseThrow { IllegalArgumentException("Contacto no encontrado: $id") }
    }

    @Transactional
    fun eliminar(id: Long) {
<<<<<<< HEAD
        if (repo.existsById(id)) {
            repo.deleteById(id)
        }
    }

    private fun sanitizarBusqueda(valor: String): String? {
        val limpio = valor
            .trim()
            .replace(Regex("\\s+"), " ")
            .replace("<", "")
            .replace(">", "")

        return limpio.takeIf { it.isNotBlank() }?.take(120)
    }

    private fun normalizarTexto(valor: String, maximo: Int, campo: String): String {
        val limpio = valor.trim().replace(Regex("\\s+"), " ")

        if (limpio.contains("<") || limpio.contains(">")) {
            throw IllegalArgumentException("$campo contiene caracteres no permitidos.")
        }

        val regex = Regex("^[\\p{L}][\\p{L}\\s'\\-]{1,${maximo - 1}}$")
        if (!regex.matches(limpio)) {
            throw IllegalArgumentException("$campo tiene un formato inválido.")
        }

        return limpio
    }

    private fun normalizarCorreo(valor: String): String {
        val limpio = valor.trim().lowercase()

        if (limpio.contains("<") || limpio.contains(">")) {
            throw IllegalArgumentException("El correo contiene caracteres no permitidos.")
        }

        if (limpio.length > 120) {
            throw IllegalArgumentException("El correo es demasiado largo.")
        }

        return limpio
    }

    private fun normalizarDigitos(valor: String, longitud: Int, campo: String): String {
        val soloDigitos = valor.filter { it.isDigit() }
        if (soloDigitos.length != longitud) {
            throw IllegalArgumentException("$campo inválido.")
        }
        return soloDigitos
    }

    private fun validarFoto(foto: MultipartFile) {
        val tiposPermitidos = setOf("image/jpeg", "image/png", "image/webp")
        val maxBytes = 2 * 1024 * 1024

        if (foto.contentType !in tiposPermitidos) {
            throw IllegalArgumentException("La foto debe ser JPG, PNG o WEBP.")
        }

        if (foto.size > maxBytes) {
            throw IllegalArgumentException("La foto no debe exceder 2 MB.")
        }
=======
        if (!repo.existsById(id)) {
            throw IllegalArgumentException("El contacto no existe")
        }
        repo.deleteById(id)
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
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
