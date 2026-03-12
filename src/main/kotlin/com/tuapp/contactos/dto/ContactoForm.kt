import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.http.MediaType
import java.io.IOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Controller
@RequestMapping("/Home")
class ContactoController @Autowired constructor(val contactoService: ContactoService) {

    @GetMapping("/Formulario")
    fun formulario(@RequestParam(required = false) id: Long?, model: Model): String {
        val contacto = if (id != null) contactoService.obtener(id) else ContactoForm("", "", "", "", "", null)
        model.addAttribute("contacto", contacto)
        return "formulario" // vista de Thymeleaf
    }

    @PostMapping("/Guardar")
    fun guardarContacto(
        @Valid @ModelAttribute contacto: ContactoForm,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
        @RequestParam("foto") foto: MultipartFile
    ): String {
        if (result.hasErrors()) {
            return "formulario" // vuelve al formulario si hay errores
        }

        val fotoBase64 = if (!foto.isEmpty) {
            val bytes = foto.bytes
            "data:${foto.contentType};base64," + java.util.Base64.getEncoder().encodeToString(bytes)
        } else {
            null
        }

        contacto.fotoBase64 = fotoBase64

        val contactoGuardado = contactoService.guardar(contacto)

        redirectAttributes.addFlashAttribute("mensaje", "Contacto guardado correctamente")
        return "redirect:/Home/Lista"
    }

    @GetMapping("/Lista")
    fun listaContactos(model: Model): String {
        val contactos = contactoService.obtenerTodos()
        model.addAttribute("contactos", contactos)
        return "lista" // vista de Thymeleaf
    }
}