package com.example.petpassport_android_app.presentation.details.Card

import PetProfileCard
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetProfileEditCard(
    pet: Pet,
    onBack: () -> Unit,
    onSave: (Pet) -> Unit,
    onUploadPhoto: (ByteArray?) -> Unit,
    isUploading: Boolean
) {
    var name by remember { mutableStateOf(pet.name) }
    var breed by remember { mutableStateOf(pet.breed) }
    var weight by remember { mutableStateOf(pet.weight.toString()) }
    var birthDateIso by remember { mutableStateOf(pet.birthDate) } // для базы данных


    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { androidUri: android.net.Uri? ->
        if (androidUri == null) return@rememberLauncherForActivityResult

        val bytes = try {
            context.contentResolver.openInputStream(androidUri)?.use { it.readBytes() }
        } catch (e: Exception) {
            Log.e("PetProfileEditCard", "Ошибка чтения фото", e)
            null
        }

        onUploadPhoto(bytes)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(top = 16.dp, start = 16.dp, end = 16.dp) // отступ сверху, чтобы статус-бар не перекрывал TopBarCard
    ) {
        // TopBarCard вместо ручного Row
        TopBarCard(
            onBack = onBack,
            iconRes = R.drawable.ic_cat,
            title = pet.name
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Фото питомца
                key(pet.photoUrl) {
                    AsyncImage(
                        model = pet.photoUrl?.takeIf { it.isNotBlank() } ?: R.drawable.avatar_pet_defualt,
                        contentDescription = "Фото питомца",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Поля редактирования
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextFieldCard(value = name, onValueChange = { name = it }, text = "Имя")
                    TextFieldCard(value = breed, onValueChange = { breed = it }, text = "Порода")
                    TextFieldCard(value = weight, onValueChange = { weight = it }, text = "Вес")

                    // 🗓️ Редактирование даты рождения с DateFieldCard
                    DateFieldCard(
                        label = "Дата рождения",
                        initialMillis = null,
                        onDateSelected = { iso ->
                            birthDateIso = iso.substringBefore('T')
                        }
                    )
                }

                // Кнопка выбора/изменения фото
                OutlinedButton(
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUploading
                ) {
                    Text(if (pet.photoUrl.isNullOrBlank()) "Добавить фото" else "Изменить фото")
                }

                if (isUploading) {
                    CircularProgressIndicator()
                }

                // Кнопка сохранения
                Button(
                    onClick = {
                        val updatedPet = pet.copy(
                            name = name,
                            breed = breed,
                            weight = weight.toDoubleOrNull() ?: pet.weight,
                            birthDate = birthDateIso
                        )
                        onSave(updatedPet)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Сохранить")
                }
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun PetProfileEditCardPreview() {
    PetProfileEditCard(
        pet =  Pet(
            id = 1,
            name = "Бастер",
            breed = "Лабрадор",
            weight = 25.0,
            birthDate = "12.03.2020",
            photoUrl = ""
        ),
        onBack = {},
        onSave = {},
        onUploadPhoto = {},
        isUploading = true
    )
}