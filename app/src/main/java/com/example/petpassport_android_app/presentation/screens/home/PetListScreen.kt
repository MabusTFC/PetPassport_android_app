package com.example.petpassport_android_app.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage


// Цвета из нового дизайна
val NewBgColor = Color(0xFFF4F5F9)
val NewPrimaryDark = Color(0xFF2E1A7A)

@Composable
fun PetListScreenContent(
    state: PetListScreenModel.PetsState,
    petStates: Map<Int, PetListScreenModel.PetCardState>,
    onRetry: () -> Unit,
    onAddPet: (Pet) -> Unit,
    onPetProfile: (Int) -> Unit,
    onRefreshPet: (Int) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val horizontalPadding = 20.dp // Единый отступ для всего экрана

    Scaffold(
        containerColor = NewBgColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 10.dp, bottom = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.top_bar_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding), // 20.dp
                    contentScale = ContentScale.FillWidth
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = NewPrimaryDark,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        // Box на весь экран для центрирования EmptyState
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is PetListScreenModel.PetsState.Loading -> LoadingCard("Загружаем...")

                is PetListScreenModel.PetsState.Empty -> {
                    // Теперь это будет ровно по центру экрана
                    EmptyPetsNewState(20.dp)
                }

                is PetListScreenModel.PetsState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 100.dp,
                            start = horizontalPadding, // 20.dp
                            end = horizontalPadding   // 20.dp
                        )
                    ) {
                        item {
                            Text(
                                text = "Ваши питомцы",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = NewPrimaryDark,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(state.pets, key = { it.id }) { pet ->
                            val cardState = petStates[pet.id] ?: PetListScreenModel.PetCardState.Loading

                            LaunchedEffect(pet.id) {
                                if (cardState is PetListScreenModel.PetCardState.Loading) {
                                    onRefreshPet(pet.id)
                                }
                            }

                            when (cardState) {
                                is PetListScreenModel.PetCardState.Loading -> PetCardLoading()
                                is PetListScreenModel.PetCardState.Success -> {
                                    // Используем новый дизайн карточки
                                    NewPetCard(
                                        pet = cardState.pet,
                                        onClick = { onPetProfile(cardState.pet.id) }
                                    )
                                }
                                is PetListScreenModel.PetCardState.Error -> PetCardError(
                                    message = cardState.message,
                                    onRetry = { onRefreshPet(pet.id) }
                                )
                            }
                        }
                    }
                }
                is PetListScreenModel.PetsState.Error -> ErrorCard(state.mess)
            }

            if (showDialog) {
                AddPetDialog(onDismiss = { showDialog = false }, onAdd = { onAddPet(it); showDialog = false })
            }
        }
    }
}

@Composable
fun EmptyPetsNewState(dp: Dp) {
    // Контейнер на весь экран, чтобы центрировать карточку
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp) // ТОТ ЖЕ ОТСТУП, ЧТО И У ТОП-БАРА
                .aspectRatio(1f), // Делаем карточку квадратной
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(0.dp) // Убираем тень, как вы хотели
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Картинка собаки
                Image(
                    painter = painterResource(id = R.drawable.no_photo_pet),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.9f), // Занимает 70% места внутри карточки
                    contentScale = ContentScale.Fit
                )

                // Сюда можно добавить текст "У вас пока нет питомцев", если нужно
            }
        }
    }
}

@Composable
fun NewPetCard(
    pet: Pet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Фото питомца (квадратное со скруглением)
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF2F3F5)
            ) {
                AsyncImage(
                    model = pet.photoUrl?.takeIf { it.isNotBlank() } ?: R.drawable.avatar_pet_defualt,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Текстовая информация
            Column {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewPrimaryDark
                )
                Text(
                    text = pet.breed,
                    fontSize = 14.sp,
                    color = Color(0xFF4A378B).copy(alpha = 0.7f) // Фиолетово-серый цвет
                )
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