package com.example.petpassport_android_app.presentation.screens.petProfile


import PetProfileCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.LoadingCard
import com.example.petpassport_android_app.presentation.details.Card.PetProfileEditCard
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetProfileScreenContent(
    state: PetProfileScreenModel.State,
    onBack: () -> Unit,
    onUpdatePet: (Pet) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) } //Тут можно true на false поменять чтобы посмотреть отображение меню при редактировании

    when (state) {
        is PetProfileScreenModel.State.Loading -> LoadingCard("Загружаем профиль питомца…")
        is PetProfileScreenModel.State.Error -> ErrorCard(state.message)
        is PetProfileScreenModel.State.Success -> {
            if (isEditing) {
                PetProfileEditCard(
                    pet = state.pet,
                    onBack = onBack,
                    onSave = {
                        onUpdatePet(it)
                        isEditing = false
                    }
                )
            } else {
                PetProfileCard(
                    pet = state.pet,
                    onBack = onBack,
                    onEditClick = { isEditing = true }
                )
            }
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
        onUpdatePet = {}
    )
}




