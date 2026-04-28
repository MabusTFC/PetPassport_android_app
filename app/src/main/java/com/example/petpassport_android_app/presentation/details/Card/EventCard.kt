package com.example.petpassport_android_app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(
    event: PetEvent,
    onClick: () -> Unit = {},
    showOverdueColor: Boolean = true,
    /** Global notifications master switch from preferences. */
    globalNotificationsEnabled: Boolean = true,
    /** Per-event flag from model (after merge with local store). */
    initialReminderEnabled: Boolean = true,
    onReminderToggle: ((Boolean) -> Unit)? = null
) {
    val primaryDark = Color(0xFF2E1A7A)
    val bellBlue = Color(0xFF5AB6FF)
    val badgeBg = Color(0xFFF2F3F7)

    val bellOn = globalNotificationsEnabled && initialReminderEnabled
    var isReminderEnabled by rememberSaveable(event.id) { mutableStateOf(bellOn) }
    LaunchedEffect(event.id, globalNotificationsEnabled, initialReminderEnabled) {
        isReminderEnabled = globalNotificationsEnabled && initialReminderEnabled
    }

    val categoryLabel = when (event) {
        is Vaccine -> "Вакцинация"
        is Treatment -> "Обработка"
        is DoctorVisit -> "Приём врача"
        else -> "Процедура"
    }

    val rawDate = event.date
    val datePart = try {
        val localDate = LocalDate.parse(
            rawDate.substringBefore("T"),
            DateTimeFormatter.ISO_LOCAL_DATE
        )
        localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: Exception) {
        rawDate.substringBefore("T")
    }

    val timePart = try {
        rawDate.substringAfter("T").substringBefore(":00Z").let {
            if (it.length == 5) it else rawDate.substringAfter("T").take(5)
        }
    } catch (e: Exception) {
        ""
    }

    val isOverdue = remember(event.date) {
        try {
            val date = LocalDate.parse(rawDate.substringBefore("T"), DateTimeFormatter.ISO_LOCAL_DATE)
            date.isBefore(LocalDate.now())
        } catch (e: Exception) { false }
    }

    val dateColor = if (isOverdue && showOverdueColor) Color(0xFFE53935) else primaryDark

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- ВЕРХНИЙ РЯД (Колокольчик + Дата) ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Колокольчик — переключатель уведомлений
                Icon(
                    painter = painterResource(
                        id = if (isReminderEnabled) R.drawable.ic_notification_on
                        else R.drawable.ic_notification_off
                    ),
                    contentDescription = if (isReminderEnabled) "Уведомление включено"
                    else "Уведомление выключено",
                    tint = if (isReminderEnabled) bellBlue else Color.LightGray,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterStart)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    if (!globalNotificationsEnabled) return@detectTapGestures
                                    isReminderEnabled = !isReminderEnabled
                                    onReminderToggle?.invoke(isReminderEnabled)
                                }
                            )
                        }
                )

                // Плашка с датой
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isOverdue && showOverdueColor) Color(0xFFE53935).copy(alpha = 0.5f)
                        else Color(0xFFE0E0E0)
                    ),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = if (isOverdue && showOverdueColor) Color(0xFFE53935) else bellBlue
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = datePart,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = dateColor
                        )
                        if (timePart.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .width(1.dp)
                                    .height(16.dp)
                                    .background(Color(0xFFE0E0E0))
                            )
                            Text(
                                text = timePart,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = dateColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = badgeBg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = categoryLabel,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 16.sp,
                    color = primaryDark,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = event.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryDark,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview(showBackground = true, name = "Vaccine — включено")
@Composable
fun EventCardVaccinePreview() {
    EventCard(
        event = Vaccine(
            id = 1,
            title = "Прививка от бешенства",
            date = "2027-05-01T13:00:00Z",
            petId = 1,
            medicine = "Nobivac Rabies"
        ),
        initialReminderEnabled = true
    )
}

@Preview(showBackground = true, name = "Vaccine — выключено")
@Composable
fun EventCardVaccineReminderOffPreview() {
    EventCard(
        event = Vaccine(
            id = 1,
            title = "Прививка от бешенства",
            date = "2027-05-01T13:00:00Z",
            petId = 1,
            medicine = "Nobivac Rabies"
        ),
        initialReminderEnabled = false
    )
}

@Preview(showBackground = true, name = "Vaccine — просрочена")
@Composable
fun EventCardVaccineOverduePreview() {
    EventCard(
        event = Vaccine(
            id = 1,
            title = "Прививка от бешенства",
            date = "2024-03-12T13:00:00Z",
            petId = 1,
            medicine = "Nobivac Rabies"
        ),
        initialReminderEnabled = true
    )
}

@Preview(showBackground = true, name = "Treatment")
@Composable
fun EventCardTreatmentPreview() {
    EventCard(
        event = Treatment(
            id = 2,
            title = "Обработка от клещей",
            date = "2027-06-15T10:00:00Z",
            petId = 1,
            remedy = "Bravecto",
            parasite = "Клещи",
            nextTreatmentDate = "2027-09-15"
        ),
        initialReminderEnabled = true
    )
}

@Preview(showBackground = true, name = "Doctor Visit")
@Composable
fun EventCardDoctorVisitPreview() {
    EventCard(
        event = DoctorVisit(
            id = 3,
            title = "Осмотр у ветеринара",
            date = "2027-04-20T15:00:00Z",
            petId = 1,
            clinic = "VetLife",
            doctor = "Иванов И.И.",
            diagnosis = "Аллергия"
        ),
        initialReminderEnabled = true
    )
}