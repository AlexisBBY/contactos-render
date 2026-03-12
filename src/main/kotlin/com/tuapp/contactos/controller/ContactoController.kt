package com.tuapp.contactos.controller

import com.tuapp.contactos.model.Contacto
import com.tuapp.contactos.service.ContactoService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
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
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fecha: LocalDate?,
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
        model.addAttribute("contacto", Contacto())
        return "formulario"
    }

    @GetMapping("/Formulario/{id}")
    fun editar(@PathVariable id: Long, model: Model): String {
        model.addAttribute("contacto", service.obtener(id))
        return "formulario"
    }

    @PostMapping("/Guardar")
    fun guardar(
        @Valid @ModelAttribute("contacto") contacto: Contacto,
        result: BindingResult,
        @RequestParam(required = false) foto: MultipartFile?,
        model: Model,
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
    }

    private fun esPeticionAjax(request: HttpServletRequest): Boolean {
        val requestedWith = request.getHeader("X-Requested-With")
        val accept = request.getHeader("Accept")
        return requestedWith == "XMLHttpRequest" || accept?.contains("application/json") == true
    }
}
