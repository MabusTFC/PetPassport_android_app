package com.example.petpassport_android_app.presentation.screens.events

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.screens.home.* // Импорт LabelText, CustomInputField, AppDropdownMenu
import java.text.SimpleDateFormat
import java.util.*

// Цвета (проверь, чтобы не дублировались, если они в PetListScreenContent)
private val NewPrimaryDark = Color(0xFF2E1A7A)
private val NewBgColor = Color(0xFFF4F5F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsDialog(
    onDismiss: () -> Unit,
    onAdd: (PetEvent) -> Unit
) {
    // Состояния полей
    var selectedType by remember { mutableStateOf("Прием") }
    var clinicName by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var dateDisplay by remember { mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())) }
    var timeDisplay by remember { mutableStateOf("13:00") }
    var diagnosis by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var directions by remember { mutableStateOf("") }

    // Напоминания
    var isReminderEnabled by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSaving by remember { mutableStateOf(false) }

    // Состояния календаря
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Дата для API в формате yyyy-MM-dd
    var dateForApi by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Date(millis)
                        dateDisplay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
                        dateForApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                    }
                    showDatePicker = false
                }) { Text("ОК", color = NewPrimaryDark) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) { DatePicker(state = datePickerState) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) },
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Добавить процедуру",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NewPrimaryDark,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Divider(color = Color(0xFFF0F0F0))

            LabelText("Выберите тип")
            AppDropdownMenu(
                options = listOf("Прием", "Вакцинация", "Лечение", "Осмотр"),
                selectedOption = selectedType,
                onOptionSelected = { selectedType = it }
            )

            LabelText("Клиника")
            CustomInputField(value = clinicName, onValueChange = { clinicName = it }, placeholder = "Введите название")

            LabelText("Врач")
            CustomInputField(value = doctorName, onValueChange = { doctorName = it }, placeholder = "Введите название")

            LabelText("Дата")
            Box(modifier = Modifier.fillMaxWidth()) {
                CustomInputField(
                    value = dateDisplay,
                    onValueChange = {},
                    placeholder = "",
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, null, tint = Color.Gray) }
                )
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }

            LabelText("Время")
            CustomInputField(value = timeDisplay, onValueChange = { timeDisplay = it }, placeholder = "13:00")

            LabelText("Диагноз")
            CustomAreaField(value = diagnosis, onValueChange = { diagnosis = it }, placeholder = "Диагноз")

            LabelText("Рекомендации")
            CustomAreaField(value = recommendations, onValueChange = { recommendations = it }, placeholder = "Рекомендации")

            LabelText("Направления")
            CustomAreaField(value = directions, onValueChange = { directions = it }, placeholder = "Направления")

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Напоминание", color = NewPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = { isReminderEnabled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF32ADE6))
                )
            }

            if (isReminderEnabled) {
                LabelText("Напоминать за")
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReminderChip("5 минут")
                    ReminderChip("1 час")
                    ReminderChip("1 день")
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, null, tint = Color.Gray)
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text("Отменить", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        // ФОРМИРУЕМ ISO ДАТУ (например: 2026-03-31T13:00:00Z)
                        val isoDateTime = "${dateForApi}T${timeDisplay}:00Z"

                        val event: PetEvent = when (selectedType) {
                            "Вакцинация" -> Vaccine(
                                id = 0,
                                title = clinicName.ifBlank { "Вакцинация" },
                                date = isoDateTime,
                                petId = 0,
                                medicine = clinicName
                            )
                            "Лечение" -> Treatment(
                                id = 0,
                                title = clinicName.ifBlank { "Лечение" },
                                date = isoDateTime,
                                petId = 0,
                                remedy = clinicName,
                                parasite = diagnosis,
                                nextTreatmentDate = null
                            )
                            else -> DoctorVisit(
                                id = 0,
                                title = selectedType,
                                date = isoDateTime,
                                petId = 0,
                                clinic = clinicName,
                                doctor = doctorName,
                                diagnosis = diagnosis
                            )
                        }

                        Log.d("AddEvent", "Отправка события: $event")
                        onAdd(event)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                    enabled = clinicName.isNotBlank()
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Добавить", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAreaField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = NewPrimaryDark,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@Composable
fun ReminderChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        color = Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            color = NewPrimaryDark
        )
    }
}