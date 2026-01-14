package com.example.petpassport_android_app.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.*
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetListScreenContent(
    state: PetListScreenModel.PetsState,
    onRetry: () -> Unit,
    onAddPet: (Pet) -> Unit,
    onPetProfile: (Int) -> Unit // прокидываем ID питомца
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // фон
        Image(
            painter = painterResource(id = R.drawable.background_empty_pets),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // основной контент
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (state) {
                is PetListScreenModel.PetsState.Loading -> LoadingCard("Загружаем питомцев...")
                is PetListScreenModel.PetsState.Error -> Column {
                    ErrorCard(state.mess)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onRetry) { Text("Повторить") }
                }
                is PetListScreenModel.PetsState.Empty -> EmptyPetsState()
                is PetListScreenModel.PetsState.Success -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.pets) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { onPetProfile(pet.id) } // ID питомца передаем
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = AppColors.Primary,
                contentColor = AppColors.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }

            if (showDialog) {
                AddPetDialog(
                    onDismiss = { showDialog = false },
                    onAdd = {
                        onAddPet(it)
                        showDialog = false
                    }
                )
            }
        }
    }
}



@Preview
@Composable
fun PetListScreenPreview() {
    PetListScreenContent(
        state = PetListScreenModel.PetsState.Empty,
        onRetry = {},
        onAddPet = {},
        onPetProfile = {1}
    )
}



@Preview(showBackground = true)
@Composable
fun PetListScreenSuccessPreview() {
    PetListScreenContent(
        state = PetListScreenModel.PetsState.Success(
            pets = listOf(
                Pet(
                    id = 1,
                    name = "Бобик",
                    breed = "Двортерьер",
                    weight = 12.5,
                    birthDate = "2021-04-12",
                    photoUrl = ""
                ),
                Pet(
                    id = 2,
                    name = "Рекс",
                    breed = "Немецкая овчарка",
                    weight = 30.0,
                    birthDate = "2020-08-01",
                    photoUrl = ""
                ),
                Pet(
                    id = 3,
                    name = "Луна",
                    breed = "Хаски",
                    weight = 22.3,
                    birthDate = "2022-01-18",
                    photoUrl = ""
                ),
                Pet(
                    id = 4,
                    name = "Бобик",
                    breed = "Двортерьер",
                    weight = 12.5,
                    birthDate = "2021-04-12",
                    photoUrl = ""
                ),
                Pet(
                    id = 5,
                    name = "Рекс",
                    breed = "Немецкая овчарка",
                    weight = 30.0,
                    birthDate = "2020-08-01",
                    photoUrl = ""
                ),
                Pet(
                    id = 6,
                    name = "Луна",
                    breed = "Хаски",
                    weight = 22.3,
                    birthDate = "2022-01-18",
                    photoUrl = ""
                ),
                Pet(
                    id = 7,
                    name = "Рекс",
                    breed = "Немецкая овчарка",
                    weight = 30.0,
                    birthDate = "2020-08-01",
                    photoUrl = ""
                ),
                Pet(
                    id = 8,
                    name = "Луна",
                    breed = "Хаски",
                    weight = 22.3,
                    birthDate = "2022-01-18",
                    photoUrl = ""
                )
            )
        ),
        onRetry = {},
        onAddPet = {},
        onPetProfile = { petId -> Log.d("Preview", "Clicked pet $petId") }
    )
}

