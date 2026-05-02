package com.example.petpassport_android_app.presentation.screens.events

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.domain.model.Event.EventReminderUiPayload
import com.example.petpassport_android_app.notification.EventNotificationPlanner
import com.example.petpassport_android_app.presentation.screens.home.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding


private val NewPrimaryDark = Color(0xFF2E1A7A)
private val NewBgColor = Color(0xFFF4F5F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsDialog(
    onDismiss: () -> Unit,
    onAdd: (PetEvent, EventReminderUiPayload) -> Unit
) {
    var selectedType by remember { mutableStateOf("Прием") }
    var title by remember { mutableStateOf("") }
    var clinicName by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var dateDisplay by remember { mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())) }
    var timeDisplay by remember { mutableStateOf("13:00") }
    var diagnosis by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var directions by remember { mutableStateOf("") }
    var medicine by remember { mutableStateOf("") }
    var remedy by remember { mutableStateOf("") }
    var parasite by remember { mutableStateOf("") }

    var isReminderEnabled by remember { mutableStateOf(true) }
    var selectedReminders by remember { mutableStateOf(setOf<Long>()) }
    var showCustomReminderDialog by remember { mutableStateOf(false) }
    var customHours by remember { mutableStateOf("") }
    var customMinutes by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSaving by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var dateForApi by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

    // --- ДАТАПИКЕР ---
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
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // --- ДИАЛОГ КАСТОМНОГО НАПОМИНАНИЯ ---
    if (showCustomReminderDialog) {
        AlertDialog(
            onDismissRequest = { showCustomReminderDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Своё время", fontWeight = FontWeight.Bold, color = NewPrimaryDark)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customHours,
                        onValueChange = { customHours = it.filter { c -> c.isDigit() } },
                        label = { Text("Часов") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NewPrimaryDark,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                    OutlinedTextField(
                        value = customMinutes,
                        onValueChange = { customMinutes = it.filter { c -> c.isDigit() } },
                        label = { Text("Минут") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NewPrimaryDark,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val totalMinutes = (customHours.toLongOrNull() ?: 0L) * 60 +
                            (customMinutes.toLongOrNull() ?: 0L)
                    if (totalMinutes > 0) {
                        selectedReminders = selectedReminders + totalMinutes
                    }
                    showCustomReminderDialog = false
                    customHours = ""
                    customMinutes = ""
                }) {
                    Text("Добавить", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCustomReminderDialog = false
                    customHours = ""
                    customMinutes = ""
                }) {
                    Text("Отмена", color = Color.Gray)
                }
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) },
        // ← убрали modifier = Modifier.fillMaxHeight(0.92f)
        contentWindowInsets = { WindowInsets(0) } // ← убираем стандартные insets
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.88f) // ← максимум 88% экрана
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .navigationBarsPadding() // ← отступ от навигационной полосы
                .imePadding(), // ← отступ от клавиатуры
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

            // Тип
            LabelText("Выберите тип")
            AppDropdownMenu(
                options = listOf("Прием", "Вакцинация", "Лечение", "Осмотр"),
                selectedOption = selectedType,
                onOptionSelected = { selectedType = it }
            )

            // Название
            LabelText("Название")
            CustomInputField(value = title, onValueChange = { title = it }, placeholder = "Введите название")

            // Специфичные поля по типу
            when (selectedType) {
                "Вакцинация" -> {
                    LabelText("Препарат")
                    CustomInputField(value = medicine, onValueChange = { medicine = it }, placeholder = "Название препарата")
                }
                "Лечение" -> {
                    LabelText("Препарат")
                    CustomInputField(value = remedy, onValueChange = { remedy = it }, placeholder = "Название препарата")
                    LabelText("Паразит")
                    CustomInputField(value = parasite, onValueChange = { parasite = it }, placeholder = "Тип паразита")
                }
                else -> {
                    LabelText("Клиника")
                    CustomInputField(value = clinicName, onValueChange = { clinicName = it }, placeholder = "Введите название")
                    LabelText("Врач")
                    CustomInputField(value = doctorName, onValueChange = { doctorName = it }, placeholder = "Введите имя")
                    LabelText("Диагноз")
                    CustomAreaField(value = diagnosis, onValueChange = { diagnosis = it }, placeholder = "Диагноз")
                    LabelText("Рекомендации")
                    CustomAreaField(value = recommendations, onValueChange = { recommendations = it }, placeholder = "Рекомендации")
                    LabelText("Направления")
                    CustomAreaField(value = directions, onValueChange = { directions = it }, placeholder = "Направления")
                }
            }

            // Дата
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

            // Время
            LabelText("Время")
            CustomInputField(value = timeDisplay, onValueChange = { timeDisplay = it }, placeholder = "13:00")

            Spacer(Modifier.height(24.dp))

            // Напоминание — переключатель
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(
                            id = if (isReminderEnabled) R.drawable.ic_notification_on
                            else R.drawable.ic_notification_off
                        ),
                        contentDescription = null,
                        tint = if (isReminderEnabled) Color(0xFF5AB6FF) else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Напоминание", color = NewPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = { isReminderEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF32ADE6)
                    )
                )
            }

            // Чипы напоминаний
            if (isReminderEnabled) {
                LabelText("Напоминать за")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Фиксированные чипы
                    listOf(5L to "5 минут", 60L to "1 час", 1440L to "1 день").forEach { (minutes, label) ->
                        val selected = minutes in selectedReminders
                        Surface(
                            modifier = Modifier.clickable {
                                selectedReminders = if (selected)
                                    selectedReminders - minutes
                                else
                                    selectedReminders + minutes
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selected) NewPrimaryDark else Color(0xFFE0E0E0)
                            ),
                            color = if (selected) NewPrimaryDark else Color.White
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 14.sp,
                                color = if (selected) Color.White else NewPrimaryDark
                            )
                        }
                    }

                    // Кнопка + кастомное время
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { showCustomReminderDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, null, tint = Color.Gray)
                        }
                    }
                }

                // Кастомные чипы
                val customReminders = selectedReminders - setOf(5L, 60L, 1440L)
                if (customReminders.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        customReminders.sorted().forEach { minutes ->
                            val label = when {
                                minutes < 60 -> "$minutes мин"
                                minutes < 1440 -> "${minutes / 60} ч"
                                else -> "${minutes / 1440} д"
                            }
                            Surface(
                                modifier = Modifier.clickable {
                                    selectedReminders = selectedReminders - minutes
                                },
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, NewPrimaryDark),
                                color = NewPrimaryDark
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(label, fontSize = 14.sp, color = Color.White)
                                    Spacer(Modifier.width(6.dp))
                                    Text("✕", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
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
                        val isoDateTime = "${dateForApi}T${timeDisplay}:00Z"

                        val offsetsSorted = selectedReminders.toList().sorted()
                        val effectiveOffsets = when {
                            !isReminderEnabled -> emptyList()
                            offsetsSorted.isNotEmpty() -> offsetsSorted
                            else -> listOf(EventNotificationPlanner.defaultOffsetIfEmptyMinutes)
                        }
                        val reminderEffective = isReminderEnabled && effectiveOffsets.isNotEmpty()
                        val event: PetEvent = when (selectedType) {
                            "Вакцинация" -> Vaccine(
                                id = 0,
                                title = title.ifBlank { "Вакцинация" },
                                date = isoDateTime,
                                petId = 0,
                                medicine = medicine,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                            "Лечение" -> Treatment(
                                id = 0,
                                title = title.ifBlank { "Лечение" },
                                date = isoDateTime,
                                petId = 0,
                                remedy = remedy,
                                parasite = parasite,
                                nextTreatmentDate = null,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                            else -> DoctorVisit(
                                id = 0,
                                title = title.ifBlank { selectedType },
                                date = isoDateTime,
                                petId = 0,
                                clinic = clinicName,
                                doctor = doctorName,
                                diagnosis = diagnosis,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                        }

                        onAdd(
                            event,
                            EventReminderUiPayload(
                                enabled = reminderEffective,
                                offsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            ),
                        )
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                    enabled = title.isNotBlank()
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
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
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

@Preview(showBackground = true, name = "Add Events Dialog")
@Composable
fun AddEventsDialogPreview() {
    MaterialTheme {
        AddEventsDialog(
            onDismiss = {},
            onAdd = { event, _ ->
                android.util.Log.d("Preview", "Добавлено событие: $event")
            }
        )
    }
}