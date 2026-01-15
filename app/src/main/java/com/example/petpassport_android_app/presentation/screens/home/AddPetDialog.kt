package com.example.petpassport_android_app.presentation.screens.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.details.Card.DateFieldCard
import com.example.petpassport_android_app.presentation.details.Card.TextFieldCard
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun AddPetDialog(
    onDismiss: () -> Unit,
    onAdd: (Pet) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var birthDateIso by remember { mutableStateOf("") } // для базы данных
    var photoUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Добавить питомца",
                color = AppColors.Primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Превью выбранного фото
                photoUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Поля для ввода данных питомца
                TextFieldCard(value = name, onValueChange = { name = it }, text = "Имя")
                TextFieldCard(value = breed, onValueChange = { breed = it }, text = "Порода")
                TextFieldCard(value = weight, onValueChange = { weight = it }, text = "Вес")

                // 🗓️ Дата рождения с DateFieldCard
                DateFieldCard(
                    label = "Дата рождения",
                    onDateSelected = { iso -> birthDateIso = iso.substringBefore('T') }
                )

                // Кнопка выбора/изменения фото
                OutlinedButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (photoUri == null) "Добавить фото"
                        else "Изменить фото"
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank() && birthDateIso.isNotBlank(),
                onClick = {
                    val pet = Pet(
                        id = 0,
                        name = name,
                        breed = breed,
                        weight = weight.toDoubleOrNull() ?: 0.0,
                        birthDate = birthDateIso,
                        photoUrl = photoUri?.toString()
                    )

                    Log.d("AddPetDialog", "Создан Pet: $pet")

                    onAdd(pet)
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AddPetDialogPreview() {
    AddPetDialog(
        onDismiss = {},
        onAdd = {}
    )
}
