import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.tuapp.contactos.model.ContactoForm

@Repository
interface ContactoRepository : JpaRepository<ContactoForm, Long>
    @Query(
        
        SELECT c FROM Contacto c
        WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:correo IS NULL OR LOWER(c.correo) LIKE LOWER(CONCAT('%', :correo, '%')))
          AND (:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%'))
          AND (:cp IS NULL OR c.cp LIKE CONCAT('%', :cp, '%'))
          AND (:fecha IS NULL OR c.fechaNacimiento = :fecha)
        
    )
    fun buscar(
        @Param("nombre") nombre: String?,
        @Param("correo") correo: String?,
        @Param("telefono") telefono: String?,
        @Param("cp") cp: String?,
        @Param("fecha") fecha: LocalDate?,
        pageable: Pageable
    ): Page<Contacto>

    fun existsByCorreoIgnoreCase(correo: String): Boolean

    fun existsByCorreoIgnoreCaseAndIdNot(correo: String, id: Long): Boolean
}
