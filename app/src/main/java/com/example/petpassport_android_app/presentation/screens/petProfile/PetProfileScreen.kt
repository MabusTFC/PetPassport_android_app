package com.example.petpassport_android_app.presentation.screens.petProfile


import PetProfileCard
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

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
    onOpenEvents: () -> Unit,
    onUploadPhoto: (ByteArray?) -> Unit,
    context: Context
) {
    when (state) {
        is PetProfileScreenModel.State.Loading -> LoadingCard("Загрузка профиля…")
        is PetProfileScreenModel.State.Error -> ErrorCard(state.message)

        is PetProfileScreenModel.State.View -> {
            key(state.pet.photoUrl) {
                PetProfileCard(
                    pet = state.pet,
                    onBack = onBack,
                    onEditProfile = onEditProfile,
                    onOpenEvents = onOpenEvents
                )
            }
        }

        is PetProfileScreenModel.State.Edit -> {
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { androidUri: Uri? ->
                if (androidUri == null) return@rememberLauncherForActivityResult

                val bytes = try {
                    context.contentResolver.openInputStream(androidUri)?.use { it.readBytes() }
                } catch (e: Exception) {
                    null
                }
                onUploadPhoto(bytes)
            }
            key(state.pet.photoUrl) {
                PetProfileEditCard(
                    pet = state.pet,
                    onBack = onBack,
                    onSave = onSavePet,
                    isUploading = state.isUploadingPhoto,
                    onUploadPhoto = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                )
            }

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
        onOpenEvents = {},
        context = LocalContext.current,
        onUploadPhoto = {}
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
        onOpenEvents = {},
        context = LocalContext.current,
        onUploadPhoto = {}
    )
}




