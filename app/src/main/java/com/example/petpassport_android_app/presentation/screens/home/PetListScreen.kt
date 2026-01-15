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
import androidx.compose.ui.graphics.Color
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
    petStates: Map<Int, PetListScreenModel.PetCardState>,
    onRetry: () -> Unit,
    onAddPet: (Pet) -> Unit,
    onPetProfile: (Int) -> Unit,
    onRefreshPet: (Int) -> Unit,
    onBack: () -> Unit // Добавляем callback для TopBar
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBarCard(
                onBack = onBack,
                iconRes = R.drawable.ic_home,
                title = "Питомцы",
                //topPadding = 16.dp // отступ сверху, чтобы статус-бар не перекрывал
            )
        },
        //containerColor = Color.Transparent // чтобы фон был виден
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // фон
            /**Image(
                painter = painterResource(id = R.drawable.background_empty_pets),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )**/

            // основной контент с padding 16.dp
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
                            // Получаем состояние карточки
                            val cardState = petStates[pet.id] ?: PetListScreenModel.PetCardState.Loading

                            // Автоматически обновляем данные, если карточка в Loading
                            LaunchedEffect(pet.id) {
                                if (cardState is PetListScreenModel.PetCardState.Loading) {
                                    onRefreshPet(pet.id)
                                }
                            }

                            when (cardState) {
                                is PetListScreenModel.PetCardState.Loading -> PetCardLoading()
                                is PetListScreenModel.PetCardState.Success -> PetCard(
                                    pet = cardState.pet,
                                    onClick = { onPetProfile(cardState.pet.id) }
                                )

                                is PetListScreenModel.PetCardState.Error -> PetCardError(
                                    message = cardState.message,
                                    onRetry = { onRefreshPet(pet.id) }
                                )
                            }
                        }
                    }
                }

                // FloatingActionButton
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

                // Диалог добавления питомца
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
}


private fun fakePet(id: Int) = Pet(
    id = id,
    name = "Питомец $id",
    breed = "Порода $id",
    weight = 5.0 + id,
    birthDate = "2020-01-0:00:00Z",
    photoUrl = null
)

@Preview(showBackground = true)
@Composable
fun PetListScreen_Empty_Preview() {
    PetListScreenContent(
        state = PetListScreenModel.PetsState.Empty,
        petStates = emptyMap(),
        onRetry = {},
        onAddPet = {},
        onPetProfile = {},
        onRefreshPet = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PetListScreen_WithPets_Preview() {

    val pets = List(8) { index ->
        fakePet(index + 1)
    }

    val petStates = pets.associate { pet ->
        pet.id to PetListScreenModel.PetCardState.Success(pet)
    }

    PetListScreenContent(
        state = PetListScreenModel.PetsState.Success(pets),
        petStates = petStates,
        onRetry = {},
        onAddPet = {},
        onPetProfile = {},
        onRefreshPet = {},
        onBack = {}
    )
}








