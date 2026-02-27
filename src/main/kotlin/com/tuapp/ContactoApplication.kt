package com.tuapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContactoApplication

fun main(args: Array<String>) {
    runApplication<ContactoApplication>(*args)
}