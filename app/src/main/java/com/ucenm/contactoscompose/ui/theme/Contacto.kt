package com.ucenm.contactoscompose.ui.theme

data class Contacto(
    val nombre: String,
    val correo: String,
    val telefono: String? = null,
    val tipo: String
)