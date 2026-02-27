package com.tuapp.contactos.controller

import com.tuapp.contactos.model.Contacto
import com.tuapp.contactos.service.ContactoService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
        @RequestParam(required = false) foto: MultipartFile?, // opcional
        model: Model
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("contacto", contacto)
            return "formulario"
        }
        service.guardar(contacto, foto)
        return "redirect:/Home/Lista"
    }

    @PostMapping("/Eliminar/{id}")
    fun eliminar(@PathVariable id: Long): String {
        service.eliminar(id)
        return "redirect:/Home/Lista"
    }
}