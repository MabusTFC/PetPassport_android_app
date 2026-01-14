package com.example.petpassport_android_app.presentation.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.details.Card.DateFieldCard
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import com.example.petpassport_android_app.presentation.details.Card.TextFieldCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsDialog(
    onDismiss: () -> Unit,
    onAdd: (PetEvent) -> Unit
) {
    var selectedType by remember { mutableStateOf("VACCINE") }
    var title by remember { mutableStateOf("") }
    var dateIso by remember { mutableStateOf("") } // дата для базы в ISO
    var extra by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить процедуру") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Выбор типа процедуры
                DropdownMenuBox(
                    selected = selectedType,
                    onSelected = { selectedType = it }
                )

                // Название процедуры
                TextFieldCard(
                    value = title,
                    onValueChange = { title = it },
                    text = "Название"
                )

                // Дата процедуры
                DateFieldCard(
                    label = "Дата",
                    onDateSelected = { iso -> dateIso = iso }
                )

                // Дополнительное поле (препарат/врач)
                TextFieldCard(
                    value = extra,
                    onValueChange = { extra = it },
                    text = when (selectedType) {
                        "VACCINE" -> "Препарат"
                        "TREATMENT" -> "Лекарство"
                        else -> "Врач"
                    }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = title.isNotBlank() && dateIso.isNotBlank(),
                onClick = {
                    val event = when (selectedType) {
                        "VACCINE" -> Vaccine(
                            id = 0,
                            title = title,
                            date = dateIso,
                            petId = 0,
                            medicine = extra
                        )
                        "TREATMENT" -> Treatment(
                            id = 0,
                            title = title,
                            date = dateIso,
                            petId = 0,
                            remedy = extra,
                            parasite = "",
                            nextTreatmentDate = null
                        )
                        else -> DoctorVisit(
                            id = 0,
                            title = title,
                            date = dateIso,
                            petId = 0,
                            clinic = "",
                            doctor = extra,
                            diagnosis = ""
                        )
                    }
                    onAdd(event)
                }
            ) {
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






