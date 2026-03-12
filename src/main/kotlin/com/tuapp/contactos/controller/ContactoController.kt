package com.tuapp.contactos.controller

import com.tuapp.contactos.dto.ContactoForm
import com.tuapp.contactos.service.ContactoService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/contactos")
class ContactoController(private val service: ContactoService) {

    @GetMapping("/Home/Lista")
    fun lista(model: Model, @RequestParam(defaultValue = "0") page: Int): String {
        val contactos = service.obtenerTodos(page)
        model.addAttribute("contactos", contactos)
        return "lista"
    }

    @GetMapping("/Formulario")
    fun nuevo(model: Model): String {
        model.addAttribute("contacto", ContactoForm())
        return "formulario"
    }

    @GetMapping("/Formulario/{id}")
    fun editar(@PathVariable id: Long, model: Model): String {
        model.addAttribute("contacto", service.obtenerFormulario(id))
        return "formulario"
    }

    @PostMapping("/Guardar")
    fun guardar(
        @Valid @ModelAttribute("contacto") contacto: ContactoForm,
        result: BindingResult,
        @RequestParam(required = false) foto: MultipartFile?,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if (result.hasErrors()) {
            contacto.fotoActualBase64 = contacto.id?.let { service.obtenerFotoBase64(it) }
            model.addAttribute("contacto", contacto)
            return "formulario"
        }

        return try {
            service.guardar(contacto, foto)
            redirectAttributes.addFlashAttribute("ok", "Contacto guardado correctamente.")
            "redirect:/Home/Lista"
        } catch (e: IllegalArgumentException) {
            contacto.fotoActualBase64 = contacto.id?.let { service.obtenerFotoBase64(it) }
            model.addAttribute("contacto", contacto)
            model.addAttribute("errorGeneral", e.message ?: "No se pudo guardar el contacto.")
            "formulario"
        }
    }

    @PostMapping("/GuardarApi", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun guardarApi(
        @Valid @ModelAttribute("contacto") contacto: ContactoForm,
        result: BindingResult,
        @RequestParam(required = false) foto: MultipartFile?
    ): ResponseEntity<Map<String, Any>> {
        if (result.hasErrors()) {
            val errores = result.fieldErrors.associate { error -> error.field to error.defaultMessage }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores)
        }
        service.guardar(contacto, foto)
        return ResponseEntity.ok(mapOf("ok" to "Contacto guardado correctamente"))
    }
}