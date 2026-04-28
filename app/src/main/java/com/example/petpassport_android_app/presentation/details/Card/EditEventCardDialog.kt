package com.example.petpassport_android_app.presentation.details.Card

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
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.notification.EventNotificationPlanner
import com.example.petpassport_android_app.presentation.screens.home.AppDropdownMenu
import com.example.petpassport_android_app.presentation.screens.home.CustomInputField
import com.example.petpassport_android_app.presentation.screens.home.LabelText
import com.example.petpassport_android_app.presentation.screens.events.CustomAreaField
import com.example.petpassport_android_app.presentation.screens.home.NewPrimaryDark
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventBottomSheet(
    event: PetEvent,
    onDismiss: () -> Unit,
    onSave: (PetEvent) -> Unit,
    onDelete: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    // Тип события
    val eventType = remember {
        when (event) {
            is Vaccine -> "Вакцинация"
            is Treatment -> "Лечение"
            is DoctorVisit -> "Приём"
            else -> "Приём"
        }
    }

    // Общие поля
    var title by remember { mutableStateOf(event.title) }
    var dateDisplay by remember {
        mutableStateOf(
            try {
                event.date.substringBefore("T").split("-").let { "${it[2]}.${it[1]}.${it[0]}" }
            } catch (e: Exception) { event.date.substringBefore("T") }
        )
    }
    var timeDisplay by remember {
        mutableStateOf(
            try { event.date.substringAfter("T").take(5) }
            catch (e: Exception) { "13:00" }
        )
    }
    var dateForApi by remember { mutableStateOf(event.date.substringBefore("T")) }

    // Vaccine
    var medicine by remember { mutableStateOf(if (event is Vaccine) event.medicine else "") }

    // Treatment
    var remedy by remember { mutableStateOf(if (event is Treatment) event.remedy else "") }
    var parasite by remember { mutableStateOf(if (event is Treatment) event.parasite else "") }

    // DoctorVisit
    var clinic by remember { mutableStateOf(if (event is DoctorVisit) event.clinic else "") }
    var doctor by remember { mutableStateOf(if (event is DoctorVisit) event.doctor else "") }
    var diagnosis by remember { mutableStateOf(if (event is DoctorVisit) event.diagnosis else "") }
    var recommendations by remember { mutableStateOf("") }
    var directions by remember { mutableStateOf("") }

    // Напоминания
    var isReminderEnabled by remember { mutableStateOf(event.reminderEnabled) }
    var selectedReminders by remember { mutableStateOf(event.reminderOffsetsMinutes.toSet()) }
    var showCustomReminderDialog by remember { mutableStateOf(false) }
    var customHours by remember { mutableStateOf("") }
    var customMinutes by remember { mutableStateOf("") }

    // DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Диалог удаления
    var showDeleteDialog by remember { mutableStateOf(false) }

    // --- ДАТАПИКЕР ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Date(millis)
                        dateDisplay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selected)
                        dateForApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected)
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
            title = { Text("Своё время", fontWeight = FontWeight.Bold, color = NewPrimaryDark) },
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
                    if (totalMinutes > 0) selectedReminders = selectedReminders + totalMinutes
                    showCustomReminderDialog = false
                    customHours = ""
                    customMinutes = ""
                }) { Text("Добавить", color = NewPrimaryDark, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCustomReminderDialog = false
                    customHours = ""
                    customMinutes = ""
                }) { Text("Отмена", color = Color.Gray) }
            }
        )
    }

    // --- ДИАЛОГ УДАЛЕНИЯ ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Удалить процедуру?", fontWeight = FontWeight.Bold, color = NewPrimaryDark) },
            text = { Text("Это действие нельзя отменить", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) { Text("Удалить", color = Color(0xFFE53935), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена", color = NewPrimaryDark)
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
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = event.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NewPrimaryDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Divider(color = Color(0xFFF0F0F0))

            // Тип (только для отображения, не меняем)
            LabelText("Тип процедуры")
            AppDropdownMenu(
                options = listOf(eventType),
                selectedOption = eventType,
                onOptionSelected = {}
            )

            // Название
            LabelText("Название")
            CustomInputField(value = title, onValueChange = { title = it }, placeholder = "Введите название")

            // Специфичные поля по типу
            when (event) {
                is Vaccine -> {
                    LabelText("Препарат")
                    CustomInputField(value = medicine, onValueChange = { medicine = it }, placeholder = "Название препарата")
                }
                is Treatment -> {
                    LabelText("Препарат")
                    CustomInputField(value = remedy, onValueChange = { remedy = it }, placeholder = "Название препарата")
                    LabelText("Паразит")
                    CustomInputField(value = parasite, onValueChange = { parasite = it }, placeholder = "Тип паразита")
                }
                is DoctorVisit -> {
                    LabelText("Клиника")
                    CustomInputField(value = clinic, onValueChange = { clinic = it }, placeholder = "Введите название")
                    LabelText("Врач")
                    CustomInputField(value = doctor, onValueChange = { doctor = it }, placeholder = "Введите имя")
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
                    placeholder = "дд.мм.гггг",
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, null, tint = Color.Gray) }
                )
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }

            // Время
            LabelText("Время")
            CustomInputField(value = timeDisplay, onValueChange = { timeDisplay = it }, placeholder = "13:00")

            Spacer(Modifier.height(24.dp))

            // Напоминание
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
                            border = BorderStroke(1.dp, if (selected) NewPrimaryDark else Color(0xFFE0E0E0)),
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

                    Surface(
                        modifier = Modifier.size(40.dp).clickable { showCustomReminderDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, null, tint = Color.Gray)
                        }
                    }
                }

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
                    .padding(bottom = 16.dp),
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
                        val updated: PetEvent = when (event) {
                            is Vaccine -> event.copy(
                                title = title,
                                date = isoDateTime,
                                medicine = medicine,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                            is Treatment -> event.copy(
                                title = title,
                                date = isoDateTime,
                                remedy = remedy,
                                parasite = parasite,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                            is DoctorVisit -> event.copy(
                                title = title,
                                date = isoDateTime,
                                clinic = clinic,
                                doctor = doctor,
                                diagnosis = diagnosis,
                                reminderEnabled = reminderEffective,
                                reminderOffsetsMinutes = if (reminderEffective) effectiveOffsets else emptyList(),
                            )
                        }

                        onSave(updated)
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                    enabled = title.isNotBlank()
                ) {
                    Text("Сохранить", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Удалить
            TextButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text("Удалить", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Edit Vaccine")
@Composable
fun EditEventVaccinePreview() {
    MaterialTheme {
        EditEventBottomSheet(
            event = Vaccine(
                id = 1,
                title = "Вакцинация от туберкулеза",
                date = "2026-07-01T13:00:00Z",
                petId = 1,
                medicine = "Nobivac"
            ),
            onDismiss = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit DoctorVisit")
@Composable
fun EditEventDoctorVisitPreview() {
    MaterialTheme {
        EditEventBottomSheet(
            event = DoctorVisit(
                id = 2,
                title = "Приём ветеринара",
                date = "2026-04-05T15:00:00Z",
                petId = 1,
                clinic = "ВетКлиник",
                doctor = "Иванова А.С.",
                diagnosis = "Здоров"
            ),
            onDismiss = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Treatment")
@Composable
fun EditEventTreatmentPreview() {
    MaterialTheme {
        EditEventBottomSheet(
            event = Treatment(
                id = 3,
                title = "Обработка от клещей",
                date = "2026-06-15T10:00:00Z",
                petId = 1,
                remedy = "Bravecto",
                parasite = "Клещи",
                nextTreatmentDate = "2026-09-15"
            ),
            onDismiss = {},
            onSave = {}
        )
    }
}