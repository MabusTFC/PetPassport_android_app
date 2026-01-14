package com.example.petpassport_android_app.presentation.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.details.Card.TextFieldCard

@Composable
fun AddEventsDialog(
    onDismiss: () -> Unit,
    onAdd: (PetEvent) -> Unit
) {
    var selectedType by remember { mutableStateOf("VACCINE") }
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить процедуру") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                DropdownMenuBox(
                    selected = selectedType,
                    onSelected = { selectedType = it }
                )

                TextFieldCard(value = title, onValueChange = { title = it }, text = "Название")
                TextFieldCard(value = date, onValueChange = { date = it }, text = "Дата")
                TextFieldCard(value = extra, onValueChange = { extra = it },
                    text = when (selectedType) {
                        "VACCINE"   -> "Препарат"
                        "TREATMENT" -> "Лекарство"
                        else        -> "Врач"
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val event = when (selectedType) {
                    "VACCINE" -> Vaccine(0, title, date, 0, extra)
                    "TREATMENT" -> Treatment(0, title, date, 0, extra, "", null)
                    else -> DoctorVisit(0, title, date, 0, "", extra, "")
                }
                onAdd(event)
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
private fun DropdownMenuBox(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(
                when (selected) {
                    "VACCINE" -> "Вакцинация"
                    "TREATMENT" -> "Лечение"
                    else -> "Визит к врачу"
                }
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Вакцинация") }, onClick = {
                onSelected("VACCINE"); expanded = false
            })
            DropdownMenuItem(text = { Text("Лечение") }, onClick = {
                onSelected("TREATMENT"); expanded = false
            })
            DropdownMenuItem(text = { Text("Визит к врачу") }, onClick = {
                onSelected("DOCTOR"); expanded = false
            })
        }
    }
}

@Preview
@Composable
fun AddEventsDialogPreview() {
    MaterialTheme {
        Surface {
            AddEventsDialog(
                onDismiss = {},
                onAdd = {}
            )
        }
    }
}






