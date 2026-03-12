package com.tuapp.contactos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContactoCreateRequest {

    // Letras (incluye acentos), espacios, puntos y guiones simples. SIN < > " ' &
    @NotBlank(message = "Nombre requerido.")
    @Size(min = 2, max = 60, message = "Nombre debe tener 2 a 60 caracteres.")
    @Pattern(
        regexp = "^[\\p{L} .'-]{2,60}$",
        message = "Nombre inválido. Solo letras/espacios y . ' -"
    )
    private String nombre;

    @NotBlank(message = "Apellidos requeridos.")
    @Size(min = 2, max = 80, message = "Apellidos debe tener 2 a 80 caracteres.")
    @Pattern(
        regexp = "^[\\p{L} .'-]{2,80}$",
        message = "Apellidos inválidos. Solo letras/espacios y . ' -"
    )
    private String apellidos;

    @NotBlank(message = "Correo requerido.")
    @Email(message = "Correo inválido.")
    @Size(max = 120, message = "Correo demasiado largo.")
    // bloquea < > " ' &
    @Pattern(
        regexp = "^[^<>&\"']{3,120}$",
        message = "Correo contiene caracteres no permitidos."
    )
    private String correo;

    @NotBlank(message = "Teléfono requerido.")
    @Size(min = 7, max = 20, message = "Teléfono debe tener 7 a 20 caracteres.")
    @Pattern(
        regexp = "^[0-9()+\\-\\s]{7,20}$",
        message = "Teléfono inválido."
    )
    private String telefono;

    @NotBlank(message = "Mensaje requerido.")
    @Size(min = 5, max = 400, message = "Mensaje debe tener 5 a 400 caracteres.")
    // Sin < > & " ' (anti HTML/JS)
    @Pattern(
        regexp = "^[^<>&\"']{5,400}$",
        message = "Mensaje contiene caracteres no permitidos (<, >, &, \", ')."
    )
    private String mensaje;

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
}