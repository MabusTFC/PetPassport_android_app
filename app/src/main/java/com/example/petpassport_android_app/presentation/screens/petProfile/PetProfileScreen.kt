package com.example.petpassport_android_app.presentation.screens.petProfile


import com.example.petpassport_android_app.presentation.details.Card.PetProfileCard
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.petpassport_android_app.R
import androidx.compose.material3.TextButton

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.domain.model.Event.PetEvent

import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.LoadingCard
import com.example.petpassport_android_app.presentation.details.Card.PetProfileEditCard


@Composable
fun PetProfileScreenContent(
    state: PetProfileScreenModel.State,
    isNotificationsEnabled: Boolean = true,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onSavePet: (Pet) -> Unit,
    onOpenEvents: () -> Unit,
    onOpenHistory: () -> Unit = {},
    onUploadPhoto: (ByteArray?) -> Unit,
    onEventClick: (PetEvent) -> Unit = {},
    onEventReminderToggle: (PetEvent, Boolean) -> Unit = { _, _ -> },
    onDismissEdit: () -> Unit,
    context: Context
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // --- ОСНОВНОЙ КОНТЕНТ ---
        when (state) {
            is PetProfileScreenModel.State.Loading -> LoadingCard("Загрузка профиля…")
            is PetProfileScreenModel.State.Error -> ErrorCard(state.message)

            is PetProfileScreenModel.State.View, is PetProfileScreenModel.State.Edit -> {
                val pet = if (state is PetProfileScreenModel.State.View) state.pet
                else (state as PetProfileScreenModel.State.Edit).pet
                val events = if (state is PetProfileScreenModel.State.View) state.events
                else (state as PetProfileScreenModel.State.Edit).events

                key(pet.photoUrl) {
                    PetProfileCard(
                        pet = pet,
                        events = events,
                        onBack = onBack,
                        onEditProfile = onEditProfile,
                        onOpenEvents = onOpenEvents,
                        onOpenHistory = onOpenHistory,
                        onEventClick = onEventClick,
                        globalNotificationsEnabled = isNotificationsEnabled,
                        onEventReminderToggle = onEventReminderToggle,
                    )
                }
            }
        }

        // --- ФИКСИРОВАННЫЙ БАР СНИЗУ ---
        if (state is PetProfileScreenModel.State.View || state is PetProfileScreenModel.State.Edit) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Предстоящие процедуры
                        TextButton(
                            onClick = onOpenEvents,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2E1A7A))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_spritz),
                                    contentDescription = null,
                                    tint = Color(0xFF4FC3F7),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Предстоящие\nпроцедуры",
                                    color = Color(0xFF2E1A7A),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        // Разделитель
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp)
                                .background(Color(0xFFE0E0E0))
                        )

                        // Медицинская история
                        TextButton(
                            onClick = onOpenHistory,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2E1A7A))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.medical_folder),
                                    contentDescription = null,
                                    tint = Color(0xFF4FC3F7),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Медицинская\nистория",
                                    color = Color(0xFF2E1A7A),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- ШТОРКА РЕДАКТИРОВАНИЯ ---
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




