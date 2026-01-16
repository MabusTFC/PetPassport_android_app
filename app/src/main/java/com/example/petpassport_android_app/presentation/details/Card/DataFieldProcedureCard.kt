package com.example.petpassport_android_app.presentation.details.Card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFieldProcedureCard(
    label: String,
    initialMillis: Long? = null,
    onDateSelected: (isoDate: String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Человеко-читаемый формат
    var displayDate by remember {
        mutableStateOf(initialMillis?.let { formatDate(it) } ?: "")
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val today = LocalDate.now()

                return selectedDate >= today
            }
        }
    )

    OutlinedTextField(
        value = displayDate,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Выбрать дату")
            }
        },
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        displayDate = formatDate(millis)               // Человекочитаемо
                        onDateSelected(formatDateForDatabase(millis)) // В ISO для базы
                    }
                    showDatePicker = false
                }) {
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



@Preview(showBackground = true)
@Composable
fun DateFieldCardProctdurePreview() {
    var selectedIso by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        DateFieldCard(
            label = "Дата рождения",
            onDateSelected = { selectedIso = it }
        )

        Spacer(Modifier.height(12.dp))

        Text("ISO для базы: $selectedIso")
    }
}