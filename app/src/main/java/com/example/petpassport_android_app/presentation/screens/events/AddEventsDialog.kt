package com.example.petpassport_android_app.presentation.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.domain.model.Event.*
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
    var date by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

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
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") }
                )

                // 📅 Поле выбора даты
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Выбрать дату"
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = extra,
                    onValueChange = { extra = it },
                    label = {
                        Text(
                            when (selectedType) {
                                "VACCINE" -> "Препарат"
                                "TREATMENT" -> "Лекарство"
                                else -> "Врач"
                            }
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = title.isNotBlank() && date.isNotBlank(),
                onClick = {
                    val event = when (selectedType) {
                        "VACCINE" -> Vaccine(0, title, formatDateForDatabase(datePickerState.selectedDateMillis!!), 0, extra)
                        "TREATMENT" -> Treatment(0, title, formatDateForDatabase(datePickerState.selectedDateMillis!!), 0, extra, "", null)
                        else -> DoctorVisit(0, title, formatDateForDatabase(datePickerState.selectedDateMillis!!), 0, "", extra, "")
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

    // 🗓️ Диалог календаря
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date = formatDate(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun formatDateForDatabase(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC")) // важно, чтобы была UTC
        .format(DateTimeFormatter.ISO_INSTANT)
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






