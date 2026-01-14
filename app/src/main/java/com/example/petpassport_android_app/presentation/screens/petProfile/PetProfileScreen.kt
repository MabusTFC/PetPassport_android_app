package com.example.petpassport_android_app.presentation.screens.petProfile


import PetProfileCard

import androidx.compose.runtime.*

import androidx.compose.ui.tooling.preview.Preview
import com.example.petpassport_android_app.domain.model.Event.PetEvent

import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.LoadingCard
import com.example.petpassport_android_app.presentation.details.Card.PetProfileEditCard


@Composable
fun PetProfileScreenContent(
    state: PetProfileScreenModel.State,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onSavePet: (Pet) -> Unit,
    onOpenEvents: () -> Unit
) {
    when (state) {
        is PetProfileScreenModel.State.Loading -> {
            LoadingCard("Загрузка профиля…")
        }

        is PetProfileScreenModel.State.Error -> {
            ErrorCard(state.message)
        }

        is PetProfileScreenModel.State.View -> {
            PetProfileCard(
                pet = state.pet,
                onBack = onBack,
                onEditProfile = onEditProfile,
                onOpenEvents = onOpenEvents
            )
        }

        is PetProfileScreenModel.State.Edit -> {
            PetProfileEditCard(
                pet = state.pet,
                onBack = onBack,
                onSave = onSavePet
            )
        }
    }
}





@Preview(showBackground = true, name = "View")
@Composable
fun PetProfileScreenViewPreview() {
    val samplePet = Pet(
        id = 1,
        name = "Бастер",
        breed = "Лабрадор",
        weight = 25.0,
        birthDate = "12.03.2020",
        photoUrl = ""
    )

    PetProfileScreenContent(
        state = PetProfileScreenModel.State.View(samplePet),
        onBack = {},
        onEditProfile = {},
        onSavePet = {},
        onOpenEvents = {}
    )
}

@Preview(showBackground = true, name = "Edit")
@Composable
fun PetProfileScreenEditPreview() {
    val samplePet = Pet(
        id = 1,
        name = "Бастер",
        breed = "Лабрадор",
        weight = 25.0,
        birthDate = "12.03.2020",
        photoUrl = ""
    )

    PetProfileScreenContent(
        state = PetProfileScreenModel.State.Edit(samplePet),
        onBack = {},
        onEditProfile = {},
        onSavePet = {},
        onOpenEvents = {}
    )
}




