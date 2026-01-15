package com.example.petpassport_android_app.presentation.details.Card


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.components.EventCard
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun EventDetailsDialog(
    event: PetEvent,
    onDismiss: () -> Unit
) {
    val type = when (event) {
        is Vaccine -> "Вакцинация"
        is Treatment -> "Лечение"
        is DoctorVisit -> "Прием врача"
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        },
        title = {
            Text(
                text = event.title,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // разнесет иконку и дату по краям
                ) {
                    // Иконка слева
                    RoundedRectangleCard(type)


                    // Дата справа
                    Text(
                        text = formatEventDate(event.date),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        fontSize = 15.sp
                    )
                }


                Spacer(Modifier.height(4.dp))

                when (event) {
                    is Vaccine -> {
                        Text("Препарат: ${event.medicine}")
                    }

                    is Treatment -> {
                        Text("Лекарство: ${event.remedy}")
                        Text("Паразит: ${event.parasite}")
                        event.nextTreatmentDate?.let {
                            Text("Следующая дата: ${formatEventDate(it)}")
                        }
                    }

                    is DoctorVisit -> {
                        Text("Клиника: ${event.clinic}")
                        Text("Врач: ${event.doctor}")
                        Text("Диагноз: ${event.diagnosis}")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EventDetailsDialogVisitView() {
    EventDetailsDialog(
        event = DoctorVisit(
            id = 3,
            title = "Осмотр у ветеринара",
            date = "20.02.2024",
            petId = 1,
            clinic = "VetLife",
            doctor = "Иванов И.И.",
            diagnosis = "Аллергия"
        ),
        onDismiss = {},
    )
}
@Preview(showBackground = true, name = "Vaccine")
@Composable
fun EventDetailsDialogVaccineView() {
    EventDetailsDialog(
        event = Vaccine(
            id = 1,
            title = "Прививка от бешенства",
            date = "12.04.2024",
            petId = 1,
            medicine = "Nobivac Rabies"
        ),
        onDismiss = {}
    )
}

@Preview(showBackground = true, name = "Treatment")
@Composable
fun EventDetailsDialogTreatmentView() {
    EventDetailsDialog(
        event = Treatment(
            id = 2,
            title = "Обработка от клещей",
            date = "05.03.2024",
            petId = 1,
            remedy = "Bravecto",
            parasite = "Клещи",
            nextTreatmentDate = "05.06.2024"
        ),
        onDismiss = {}
    )
}




