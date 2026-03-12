package com.tuapp.contactos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactoViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/formulario";
    }

    @GetMapping("/formulario")
    public String formulario() {
        return "formulario";
    }
}