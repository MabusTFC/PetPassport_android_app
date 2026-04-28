package com.example.petpassport_android_app.presentation.screens.medicalHistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.petpassport_android_app.presentation.components.EventCard
import com.example.petpassport_android_app.presentation.screens.home.NewPrimaryDark

@Composable
fun MedicalHistoryScreenContent(
    state: MedicalHistoryScreenModel.State,
    petName: String = "",
    isNotificationsEnabled: Boolean = true,
    onBack: () -> Unit,
    onEventClick: (PetEvent) -> Unit = {},
    onEventReminderToggle: (PetEvent, Boolean) -> Unit = { _, _ -> },
) {
    Scaffold(
        containerColor = Color(0xFFF3F3F8),
        topBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onBack) {
                        Icon(Icons.Default.KeyboardArrowLeft, null, tint = NewPrimaryDark)
                        Text("Назад", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cat),
                            contentDescription = null,
                            tint = NewPrimaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = petName,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                is MedicalHistoryScreenModel.State.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is MedicalHistoryScreenModel.State.Error ->
                    Text(state.message, Modifier.align(Alignment.Center))

                is MedicalHistoryScreenModel.State.Success -> {
                    if (state.events.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.medical_folder),
                                contentDescription = null,
                                tint = NewPrimaryDark.copy(alpha = 0.3f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "История пуста",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 80.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "Медицинская история",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NewPrimaryDark,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(state.events) { event ->
                                EventCard(
                                    event = event,
                                    onClick = { onEventClick(event) },
                                    showOverdueColor = false,
                                    globalNotificationsEnabled = isNotificationsEnabled,
                                    initialReminderEnabled = event.reminderEnabled,
                                    onReminderToggle = { enabled ->
                                        onEventReminderToggle(event, enabled)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "История с событиями")
@Composable
fun MedicalHistoryScreenSuccessPreview() {
    val fakeEvents = listOf(
        Vaccine(
            id = 1,
            title = "Вакцинация от бешенства",
            date = "2024-03-12",
            petId = 1,
            medicine = "Rabican"
        ),
        Treatment(
            id = 2,
            title = "Лечение от блох",
            date = "2024-04-01",
            petId = 1,
            remedy = "Frontline",
            parasite = "Блохи",
            nextTreatmentDate = "2024-05-01"
        ),
        DoctorVisit(
            id = 3,
            title = "Осмотр у ветеринара",
            date = "2024-02-15",
            petId = 1,
            clinic = "VetLife",
            doctor = "Иванов И.И.",
            diagnosis = "Здоров"
        )
    )

    MaterialTheme {
        MedicalHistoryScreenContent(
            state = MedicalHistoryScreenModel.State.Success(fakeEvents),
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "История пуста")
@Composable
fun MedicalHistoryScreenEmptyPreview() {
    MaterialTheme {
        MedicalHistoryScreenContent(
            state = MedicalHistoryScreenModel.State.Success(emptyList()),
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
fun MedicalHistoryScreenLoadingPreview() {
    MaterialTheme {
        MedicalHistoryScreenContent(
            state = MedicalHistoryScreenModel.State.Loading,
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Ошибка")
@Composable
fun MedicalHistoryScreenErrorPreview() {
    MaterialTheme {
        MedicalHistoryScreenContent(
            state = MedicalHistoryScreenModel.State.Error("Ошибка загрузки истории"),
            petName = "Соня",
            onBack = {}
        )
    }
}