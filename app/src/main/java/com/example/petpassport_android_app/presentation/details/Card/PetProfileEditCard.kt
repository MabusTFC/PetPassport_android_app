package com.example.petpassport_android_app.presentation.details.Card

import PetProfileCard
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onSave: (Pet) -> Unit
) {
    var name by remember { mutableStateOf(pet.name) }
    var breed by remember { mutableStateOf(pet.breed) }
    var weight by remember { mutableStateOf(pet.weight.toString()) }
    var birthDateIso by remember { mutableStateOf(pet.birthDate) } // для базы данных
    var photoUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(top = 16.dp, start = 16.dp, end = 16.dp) // отступ сверху, чтобы статус-бар не перекрывал TopBarCard
    ) {
        // TopBarCard вместо ручного Row
        TopBarCard(
            onBack = onBack,
            iconRes = R.drawable.ic_cat, // иконка экрана
            title = pet.name
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // внешний отступ Box
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), // расстояние между элементами
                horizontalAlignment = Alignment.CenterHorizontally, // центруем по горизонтали
                modifier = Modifier.fillMaxWidth()
            ) {
                // Фото питомца
                AsyncImage(
                    model = when {
                        photoUri != null -> photoUri.toString()
                        !pet.photoUrl.isNullOrBlank() -> pet.photoUrl
                        else -> R.drawable.avatar_pet_defualt
                    },
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

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
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (photoUri == null) "Добавить фото" else "Изменить фото")
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
        onSave = {}
    )
}