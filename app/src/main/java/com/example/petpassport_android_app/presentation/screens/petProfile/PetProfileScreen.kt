package com.example.petpassport_android_app.presentation.screens.petProfile


import PetProfileCard

import androidx.compose.runtime.*

import androidx.compose.ui.tooling.preview.Preview
import com.example.petpassport_android_app.domain.model.Event.PetEvent

import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.LoadingCard



@Composable
fun PetProfileScreenContent(
    state: PetProfileScreenModel.State,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenEvents: (Int) -> Unit,
    //onAddEvent: (PetEvent) -> Unit
) {
    when (state) {
        is PetProfileScreenModel.State.Loading -> {
            LoadingCard("Загружаем профиль питомца…")
        }

        is PetProfileScreenModel.State.Error -> {
            ErrorCard(state.message)
        }

        is PetProfileScreenModel.State.Success -> {
            PetProfileCard(
                pet = state.pet,
                onBack = onBack,
                onEditProfile = onEditProfile,
                onOpenEvents = { onOpenEvents(state.pet.id) },
                //onAddEvent = { onAddEvent() }
            )
        }

        else -> {}
    }
}




@Preview(showBackground = true)
@Composable
fun PetProfileScreenPreview() {
    val samplePet = Pet(
        id = 1,
        name = "Бастер",
        breed = "Лабрадор",
        weight = 25.0,
        birthDate = "12.03.2020",
        photoUrl = ""
    )

    PetProfileScreenContent(
        state = PetProfileScreenModel.State.Success(samplePet),
        onBack = {},
        onEditProfile = {},
        onOpenEvents = {},
        //onAddEvent = {}
    )
}




