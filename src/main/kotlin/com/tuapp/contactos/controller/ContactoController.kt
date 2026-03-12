package com.tuapp.contactos.controller

import com.tuapp.contactos.dto.ContactoForm
import com.tuapp.contactos.service.ContactoService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate

@Controller
@RequestMapping("/Home")
class ContactoController(private val service: ContactoService) {

    @GetMapping("/Lista")
    fun lista(
        model: Model,
        @RequestParam(defaultValue = "") nombre: String,
        @RequestParam(defaultValue = "") correo: String,
        @RequestParam(defaultValue = "") telefono: String,
        @RequestParam(defaultValue = "") cp: String,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        fecha: LocalDate?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): String {

        val pagina = service.buscar(nombre, correo, telefono, cp, fecha, page, size)

        model.addAttribute("contactos", pagina)
        model.addAttribute("nombre", nombre)
        model.addAttribute("correo", correo)
        model.addAttribute("telefono", telefono)
        model.addAttribute("cp", cp)
        model.addAttribute("fecha", fecha)
        model.addAttribute("page", page)
        model.addAttribute("size", size)

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

    @PostMapping(
        "/GuardarApi",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun guardarApi(
        @Valid @ModelAttribute("contacto") contacto: ContactoForm,
        result: BindingResult,
        @RequestParam(required = false) foto: MultipartFile?
    ): ResponseEntity<Map<String, Any>> {

        if (result.hasErrors()) {
            val errores = result.fieldErrors.associate { error ->
                error.field to (error.defaultMessage ?: "Valor inválido.")
            }

            return ResponseEntity.badRequest().body(
                mapOf(
                    "ok" to false,
                    "errors" to errores
                )
            )
        }

        return try {
            service.guardar(contacto, foto)
            ResponseEntity.ok(
                mapOf(
                    "ok" to true,
                    "mensaje" to "Contacto guardado correctamente.",
                    "redirectUrl" to "/Home/Lista"
                )
            )
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                mapOf(
                    "ok" to false,
                    "generalError" to (e.message ?: "No se pudo guardar el contacto.")
                )
            )
        }
    }

    @PostMapping("/Eliminar/{id}")
    fun eliminar(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        service.eliminar(id)
        redirectAttributes.addFlashAttribute("ok", "Contacto eliminado correctamente.")
        return "redirect:/Home/Lista"
    }
}