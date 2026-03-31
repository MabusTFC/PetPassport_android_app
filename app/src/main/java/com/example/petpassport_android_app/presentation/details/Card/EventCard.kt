package com.example.petpassport_android_app.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.details.Card.EventDetailsDialog

import com.example.petpassport_android_app.presentation.details.Card.RoundedRectangleCard
import com.example.petpassport_android_app.presentation.details.Card.eventIconRes
import com.example.petpassport_android_app.presentation.details.Card.formatEventDate
import com.example.petpassport_android_app.presentation.theme.AppColors
import com.example.petpassport_android_app.R



@Composable
fun EventCard(
    event: PetEvent
) {
    // Цвета из макета
    val primaryDark = Color(0xFF2E1A7A)
    val bellBlue = Color(0xFF5AB6FF)
    val badgeBg = Color(0xFFF2F3F7)
    val borderColor = Color(0xFFE0E0E0)

    // Определяем текст категории в зависимости от типа события
    val categoryLabel = when (event) {
        is Vaccine -> "Вакцинация"
        is Treatment -> "Обработка"
        is DoctorVisit -> "Приём врача"
        else -> "Процедура"
    }

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- ВЕРХНИЙ РЯД (Иконка + Дата) ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification_on),
                    contentDescription = null,
                    tint = bellBlue,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterStart)
                )

                // Плашка с датой
                Surface(
                    shape = RoundedCornerShape(32.dp), // Максимальное скругление (овал)
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)), // Тонкая серая рамка
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 📅 Иконка календаря с часами
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar), // Убедитесь, что это иконка как на фото
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = primaryDark
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // 🗓 Текст даты (например: 30 сентября 2025)
                        Text(
                            text = event.date.substringBefore("|").trim(), // Берем часть до разделителя
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = primaryDark
                        )

                        // 📏 Вертикальный разделитель (серая линия)
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .width(1.dp)
                                .height(16.dp)
                                .background(Color(0xFFE0E0E0))
                        )

                        // ⏰ Текст времени (например: 13:00)
                        Text(
                            text = event.date.substringAfter("|").trim(), // Берем часть после разделителя
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = primaryDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- КАТЕГОРИЯ ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = badgeBg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    // ДИНАМИЧЕСКАЯ КАТЕГОРИЯ
                    text = categoryLabel,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 16.sp,
                    color = primaryDark,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ЗАГОЛОВОК ---
            Text(
                // ДИНАМИЧЕСКИЙ ЗАГОЛОВОК
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



@Preview(showBackground = true, name = "Vaccine")
@Composable
fun EventCardVaccinePreview() {
    EventCard(
        event = Vaccine(
            id = 1,
            title = "Прививка от бешенства",
            date = "12.04.2024",
            petId = 1,
            medicine = "Nobivac Rabies"
        ),
        //onClick = {}
    )
}

@Preview(showBackground = true, name = "Treatment")
@Composable
fun EventCardTreatmentPreview() {
    EventCard(
        event = Treatment(
            id = 2,
            title = "Обработка от клещей",
            date = "05.03.2024",
            petId = 1,
            remedy = "Bravecto",
            parasite = "Клещи",
            nextTreatmentDate = "05.06.2024"
        ),
        //onClick = {}
    )
}

@Preview(showBackground = true, name = "Doctor Visit")
@Composable
fun EventCardDoctorVisitPreview() {
    EventCard(
        event = DoctorVisit(
            id = 3,
            title = "Осмотр у ветеринара",
            date = "20.02.2024",
            petId = 1,
            clinic = "VetLife",
            doctor = "Иванов И.И.",
            diagnosis = "Аллергия"
        ),
        //onClick = {}
    )
}


