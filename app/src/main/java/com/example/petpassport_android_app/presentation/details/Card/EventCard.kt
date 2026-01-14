package com.example.petpassport_android_app.presentation.components

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
import androidx.compose.ui.tooling.preview.Preview
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.theme.AppColors


@Composable
fun EventCard(
    event: PetEvent,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // Заголовок
            Text(
                text = event.title,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary
            )

            Text(text = "Дата: ${event.date}")

            when (event) {
                is Vaccine -> {
                    Text("Тип: Вакцинация")
                    Text("Препарат: ${event.medicine}")
                }

                is Treatment -> {
                    Text("Тип: Лечение")
                    Text("Препарат: ${event.remedy}")
                    Text("Паразит: ${event.parasite}")
                    event.nextTreatmentDate?.let {
                        Text("Следующая дата: $it")
                    }
                }

                is DoctorVisit -> {
                    Text("Тип: Визит к врачу")
                    Text("Клиника: ${event.clinic}")
                    Text("Врач: ${event.doctor}")
                    Text("Диагноз: ${event.diagnosis}")
                }
            }
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
        onClick = {}
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
        onClick = {}
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
        onClick = {}
    )
}


