package com.example.petpassport_android_app.presentation.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.domain.model.Event.EventReminderUiPayload
import com.example.petpassport_android_app.presentation.components.EventCard
import com.example.petpassport_android_app.presentation.theme.AppColors
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.presentation.details.Card.TopBarCard
import com.example.petpassport_android_app.presentation.screens.home.NewPrimaryDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreenContent(
    state: EventsScreenModel.State,
    petName: String = "",
    isNotificationsEnabled: Boolean = true,
    onBack: () -> Unit,
    onAddEvent: (PetEvent, EventReminderUiPayload) -> Unit,
    onEventClick: (PetEvent) -> Unit = {},
    onCompleteEvent: (PetEvent) -> Unit = {},
    onToggleNotifications: (Boolean) -> Unit = {},
    onEventReminderToggle: (PetEvent, Boolean) -> Unit = { _, _ -> },
) {
    var showDialog by remember { mutableStateOf(false) }

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
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = NewPrimaryDark,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                is EventsScreenModel.State.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is EventsScreenModel.State.Error ->
                    Text(state.message, Modifier.align(Alignment.Center))

                is EventsScreenModel.State.Success -> {
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
                                text = "Предстоящие процедуры",
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
                                globalNotificationsEnabled = isNotificationsEnabled,
                                initialReminderEnabled = event.reminderEnabled,
                                onReminderToggle = { enabled ->
                                    onEventReminderToggle(event, enabled)
                                }
                            )
                        }
                    }
                }

                EventsScreenModel.State.Saving ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                else -> {}
            }

            if (showDialog) {
                AddEventsDialog(
                    onDismiss = { showDialog = false },
                    onAdd = { newEvent, reminderPayload ->
                        onAddEvent(newEvent, reminderPayload)
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenSuccessPreview() {

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
        ),
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
        ),
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

    EventsScreenContent(
        state = EventsScreenModel.State.Success(
            //pet = Pet(
            //    id = 1,
            //    name = "Бобик",
            //    breed = "Двортерьер",
            //    weight = 12.5,
            //    birthDate = "2021-04-12",
            //    photoUrl = ""
            //),
            events = fakeEvents
        ),
        onBack = {},
        onAddEvent = { _, _ -> }
    )
}


