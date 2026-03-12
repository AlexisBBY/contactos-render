package com.tuapp.contactos.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "contactos",
    indexes = {
        @Index(name = "idx_contactos_correo", columnList = "correo")
    }
)
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellidos;

    @Column(nullable = false, length = 120)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 400)
    private String mensaje;

    @Column(nullable = false, updatable = false)
    private Instant creadoEn = Instant.now();

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Instant getCreadoEn() { return creadoEn; }
}