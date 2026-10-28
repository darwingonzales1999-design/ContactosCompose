package com.ucenm.contactoscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.ucenm.contactoscompose.ui.theme.Contacto
import com.ucenm.contactoscompose.ui.theme.ContactosComposeTheme
import com.ucenm.contactoscompose.ui.theme.ValidadorJava
import androidx.compose.foundation.lazy.items
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactosComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioContactos(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

val TIPOS_CONTACTO = listOf("Personal", "Trabajo", "Otro")
@Composable
fun FormularioContactos(modifier: Modifier = Modifier) {
  var nombre by remember { mutableStateOf("")}
    var correo by remember { mutableStateOf("")}
    var telefono by remember { mutableStateOf("")}
    var tipo by remember { mutableStateOf(TIPOS_CONTACTO[0])}
    var intentoEnviar by remember {mutableStateOf(false)}
    val contactos = remember {mutableStateListOf<Contacto>()}

    val nombreError = intentoEnviar && !ValidadorJava.nombreValido(nombre)
    val correoError = intentoEnviar && !ValidadorJava.correoValido(correo)
    val telefonoError = intentoEnviar && !ValidadorJava.telefonoValido(telefono)

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it},
            label = { Text("Nombre completo") },
            isError = nombreError,
            supportingText = { if (nombreError) Text("El nombre es obligatorio")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            isError = correoError,
            supportingText = {
                if (correoError) Text(
                    if (correo.isBlank()) "El correo es obligatorio" else "Formato de correo invalido"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Telefono (opcional, 8 digitos)")},
            isError = telefonoError,
            supportingText = { if (telefonoError) Text("Debe tener exactamente 8 digitos")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tipo")
        Row(verticalAlignment = Alignment.CenterVertically) {
            TIPOS_CONTACTO.forEach { opcion ->
                RadioButton(selected = tipo == opcion, onClick = { tipo = opcion})
                Text(opcion, modifier = Modifier.padding(end = 12.dp))
            }
        }

        Button(
            onClick = {
                intentoEnviar = true
                if (ValidadorJava.nombreValido(nombre) &&
                    ValidadorJava.correoValido(correo) &&
                    ValidadorJava.telefonoValido(telefono)
                ) {
                    contactos.add(
                        Contacto(
                            nombre = nombre.trim(),
                            correo = correo.trim(),
                            telefono = telefono.trim().ifEmpty { null },
                            tipo = tipo
                        )
                    )
                    nombre = ""; correo = ""; telefono = ""
                    tipo = TIPOS_CONTACTO[0]
                    intentoEnviar = false
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Guardar Contacto")
        }

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contactos) { c ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(c.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(c.correo)
                        Text(c.telefono ?: "Sin teléfono")
                        Text(c.tipo, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }


    }

}