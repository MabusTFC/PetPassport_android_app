package com.example.petpassport_android_app.presentation.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.presentation.components.EventCard
import com.example.petpassport_android_app.presentation.theme.AppColors
import com.example.petpassport_android_app.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreenContent(
    state: EventsScreenModel.State,
    onBack: () -> Unit,
    onAddEvent: (PetEvent) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "  |  ",
                            color = AppColors.TextSecondary
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_spritz),
                            contentDescription = "",
                            modifier = Modifier
                                .size(15.dp),
                            tint = AppColors.TextSecondary
                        )
                        Text(
                            text = "   Процедуры   ",
                            color = AppColors.TextSecondary
                        )
                    } },


                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .width(150.dp)
                            .height(40.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Назад",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = AppColors.Primary,
                contentColor = AppColors.White,
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
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.events) { event ->
                            EventCard(
                                event,
                                //onClick = {}
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
                    onAdd = { newEvent ->
                        onAddEvent(newEvent)
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
        onAddEvent = {}
    )
}


