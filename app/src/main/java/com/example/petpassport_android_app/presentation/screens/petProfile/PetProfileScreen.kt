package com.example.petpassport_android_app.presentation.screens.petProfile


import com.example.petpassport_android_app.presentation.details.Card.PetProfileCard
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.tooling.preview.Preview

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
    onOpenEvents: () -> Unit,
    onUploadPhoto: (ByteArray?) -> Unit,
    onDismissEdit: () -> Unit,
    context: Context
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновый слой (Просмотр или Состояние под шторкой)
        when (state) {
            is PetProfileScreenModel.State.Loading -> LoadingCard("Загрузка профиля…")
            is PetProfileScreenModel.State.Error -> ErrorCard(state.message)

            is PetProfileScreenModel.State.View, is PetProfileScreenModel.State.Edit -> {
                val pet = if (state is PetProfileScreenModel.State.View) state.pet else (state as PetProfileScreenModel.State.Edit).pet
                val events = if (state is PetProfileScreenModel.State.View) state.events else (state as PetProfileScreenModel.State.Edit).events

                key(pet.photoUrl) {
                    PetProfileCard(
                        pet = pet,
                        events = events, // Передаем список событий
                        onBack = onBack,
                        onEditProfile = onEditProfile,
                        onOpenEvents = onOpenEvents
                    )
                }
            }
        }

        // Слой шторки редактирования
        if (state is PetProfileScreenModel.State.Edit) {
            PetProfileEditCard(
                pet = state.pet,
                onBack = onDismissEdit,
                onSave = onSavePet,
                isUploading = state.isUploadingPhoto,
                onUploadPhoto = onUploadPhoto
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

    MaterialTheme { // Обязательно для ModalBottomSheet и стилей
        PetProfileScreenContent(
            state = PetProfileScreenModel.State.View(samplePet),
            onBack = {},
            onEditProfile = {},
            onSavePet = {},
            onOpenEvents = {},
            onDismissEdit = {}, // Добавили новый параметр
            context = LocalContext.current,
            onUploadPhoto = {}
        )
    }
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

    MaterialTheme {
        PetProfileScreenContent(
            state = PetProfileScreenModel.State.Edit(samplePet),
            onBack = {},
            onEditProfile = {},
            onSavePet = {},
            onOpenEvents = {},
            onDismissEdit = {}, // Добавили новый параметр
            context = LocalContext.current,
            onUploadPhoto = {}
        )
    }
}




