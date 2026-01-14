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
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFieldCard(
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
        initialSelectedDateMillis = initialMillis
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

    // Диалог календаря
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

/** Человекочитаемый формат для UI */
private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

/** ISO 8601 формат для базы */
fun formatDateForDatabase(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC"))
        .format(DateTimeFormatter.ISO_INSTANT)
}

@Preview(showBackground = true)
@Composable
fun DateFieldCardPreview() {
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