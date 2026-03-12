package com.tuapp.contactos.controller

import com.tuapp.contactos.dto.ContactoForm
import com.tuapp.contactos.service.ContactoService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
<<<<<<< HEAD
import org.springframework.http.MediaType
=======
import org.springframework.http.HttpStatus
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
<<<<<<< HEAD
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
=======
        request: HttpServletRequest,
        redirect: RedirectAttributes
    ): Any {
        if (result.hasErrors()) {
            return if (esPeticionAjax(request)) {
                ResponseEntity.badRequest().body(
                    mapOf(
                        "ok" to false,
                        "mensaje" to "Revisa los campos del formulario",
                        "errores" to result.fieldErrors.associate {
                            it.field to (it.defaultMessage ?: "Valor inválido")
                        }
                    )
                )
            } else {
                model.addAttribute("contacto", contacto)
                "formulario"
            }
        }

        return try {
            service.guardar(contacto, foto)

            if (esPeticionAjax(request)) {
                ResponseEntity.ok(
                    mapOf(
                        "ok" to true,
                        "mensaje" to "Contacto guardado correctamente",
                        "redirectUrl" to "/Home/Lista"
                    )
                )
            } else {
                redirect.addFlashAttribute("ok", "Contacto guardado correctamente")
                "redirect:/Home/Lista"
            }
        } catch (ex: IllegalArgumentException) {
            if (esPeticionAjax(request)) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    mapOf(
                        "ok" to false,
                        "mensaje" to (ex.message ?: "No fue posible guardar el contacto")
                    )
                )
            } else {
                model.addAttribute("contacto", contacto)
                model.addAttribute("errorGeneral", ex.message)
                "formulario"
            }
        }
    }

    @PostMapping("/Eliminar/{id}")
    fun eliminar(@PathVariable id: Long, redirect: RedirectAttributes): String {
        return try {
            service.eliminar(id)
            redirect.addFlashAttribute("ok", "Contacto eliminado correctamente")
            "redirect:/Home/Lista"
        } catch (ex: IllegalArgumentException) {
            redirect.addFlashAttribute("error", ex.message)
            "redirect:/Home/Lista"
        }
>>>>>>> b8d8e277a2e834a4791e594e7d4ac9fbe6c936dd
    }

    private fun esPeticionAjax(request: HttpServletRequest): Boolean {
        val requestedWith = request.getHeader("X-Requested-With")
        val accept = request.getHeader("Accept")
        return requestedWith == "XMLHttpRequest" || accept?.contains("application/json") == true
    }
}
