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
    petStates: Map<Int, PetListScreenModel.PetCardState>,
    onRetry: () -> Unit,
    onAddPet: (Pet) -> Unit,
    onPetProfile: (Int) -> Unit,
    onRefreshPet: (Int) -> Unit
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






