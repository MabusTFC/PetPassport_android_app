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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.details.Card.EventDetailsDialog

import com.example.petpassport_android_app.presentation.details.Card.RoundedRectangleCard
import com.example.petpassport_android_app.presentation.details.Card.eventIconRes
import com.example.petpassport_android_app.presentation.details.Card.formatEventDate
import com.example.petpassport_android_app.presentation.theme.AppColors



@Composable
fun EventCard(
    event: PetEvent
) {
    var showDetails by remember { mutableStateOf(false) }

    val type = when (event) {
        is Vaccine -> "Вакцинация"
        is Treatment -> "Лечение"
        is DoctorVisit -> "Прием врача"
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { showDetails = true }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🧩 ИКОНКА
            Box(
                modifier = Modifier
                    .size(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = eventIconRes(event)),
                    contentDescription = null,
                    tint = AppColors.Primary,
                    modifier = Modifier.size(25.dp)
                )

            }

            Spacer(Modifier.width(16.dp))

            // 📄 КОНТЕНТ
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = event.title,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formatEventDate(event.date),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(6.dp))


                RoundedRectangleCard(type)


            }
        }
    }


    if (showDetails) {
        EventDetailsDialog(
            event = event,
            onDismiss = { showDetails = false }
        )
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


