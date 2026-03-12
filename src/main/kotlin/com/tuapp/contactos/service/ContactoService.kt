import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import com.tuapp.contactos.model.ContactoForm
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Page

@Service
class ContactoService @Autowired constructor(val contactoRepository: ContactoRepository) {

    fun guardar(contacto: ContactoForm): ContactoForm {
        // Lógica de guardar contacto
        return contactoRepository.save(contacto)
    }

    fun obtener(id: Long): ContactoForm {
        return contactoRepository.findById(id).orElseThrow { RuntimeException("Contacto no encontrado") }
    }

    fun obtenerTodos(): List<ContactoForm> {
        return contactoRepository.findAll()
    }

    // Aquí puedes agregar otros métodos según las necesidades de tu aplicación
}