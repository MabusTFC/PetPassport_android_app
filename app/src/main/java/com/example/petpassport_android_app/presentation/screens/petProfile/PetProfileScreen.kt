package com.example.petpassport_android_app.presentation.screens.petProfile


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.LoadingCard
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetProfileScreenContent(
    state: PetProfileScreenModel.State,
    onBack: () -> Unit
) {
    when (state) {
        is PetProfileScreenModel.State.Loading -> {
            LoadingCard("Загружаем профиль питомца…")
        }

        is PetProfileScreenModel.State.Error -> {
            ErrorCard(state.message)
        }

        is PetProfileScreenModel.State.Success -> {
            val pet = state.pet

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }

                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = pet.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    pet.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AppColors.Primary
                )

                Text("Порода: ${pet.breed}")
                Text("Вес: ${pet.weight} кг")
                Text("Дата рождения: ${pet.birthDate}")
            }
        }
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
        onBack = {}
    )
}


