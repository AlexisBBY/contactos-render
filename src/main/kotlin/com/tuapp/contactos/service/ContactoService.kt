package com.tuapp.contactos.service

import com.tuapp.contactos.model.Contacto
import com.tuapp.contactos.repository.ContactoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Base64

@Service
class ContactoService(private val repository: ContactoRepository) {

    fun obtenerFormulario(id: Long): ContactoForm {
        val contacto = repository.findById(id).orElseThrow { IllegalArgumentException("Contacto no encontrado") }
        return ContactoForm(contacto)
    }

    fun obtenerTodos(page: Int) = repository.findAll(PageRequest.of(page, 10))

    fun guardar(contacto: ContactoForm, foto: MultipartFile?) {
        val entity = Contacto(
            id = contacto.id,
            nombre = contacto.nombre,
            apellidos = contacto.apellidos,
            correo = contacto.correo,
            telefono = contacto.telefono,
            cp = contacto.cp,
            fechaNacimiento = contacto.fechaNacimiento
        )
        foto?.let {
            entity.fotoActualBase64 = Base64.getEncoder().encodeToString(it.bytes)
        }
        repository.save(entity)
    }
}