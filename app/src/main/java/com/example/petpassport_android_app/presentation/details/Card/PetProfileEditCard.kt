package com.example.petpassport_android_app.presentation.details.Card

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.screens.home.* // Импорт вспомогательных компонентов
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileEditCard(
    pet: Pet,
    onBack: () -> Unit,
    onSave: (Pet) -> Unit,
    onUploadPhoto: (ByteArray?) -> Unit,
    isUploading: Boolean
) {
    // Состояния полей (подтягиваем текущие данные питомца)
    var name by remember { mutableStateOf(pet.name) }
    var breed by remember { mutableStateOf(pet.breed) }
    var weight by remember { mutableStateOf(pet.weight.toString()) }
    var birthDateDisplay by remember { mutableStateOf(pet.birthDate) } // Для отображения
    var birthDateApi by remember { mutableStateOf(pet.birthDate) }    // Для отправки на сервер

    var selectedAnimal by remember { mutableStateOf("Кот") }
    var selectedGender by remember { mutableStateOf("Женский") }

    // Календарь
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    // Выбор фото
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val bytes = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) { null }
        onUploadPhoto(bytes)
    }

    // Логика календаря
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        birthDateDisplay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
                        birthDateApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                    }
                    showDatePicker = false
                }) { Text("ОК", color = NewPrimaryDark, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // ВСПЛЫВАЮЩЕЕ ОКНО (BottomSheet)
    ModalBottomSheet(
        onDismissRequest = onBack,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) },
        contentWindowInsets = { WindowInsets(0) } // Занимает почти весь экран
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.88f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Изменить данные",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NewPrimaryDark,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(16.dp))

            // Поля ввода
            LabelText("Выберите животное")
            AppDropdownMenu(
                options = listOf("Кот", "Собака", "Птица", "Другое"),
                selectedOption = selectedAnimal,
                onOptionSelected = { selectedAnimal = it }
            )

            LabelText("Имя")
            CustomInputField(value = name, onValueChange = { name = it }, placeholder = "Введите имя")

            LabelText("Порода")
            CustomInputField(value = breed, onValueChange = { breed = it }, placeholder = "Введите название")

            LabelText("Дата рождения")
            Box(modifier = Modifier.fillMaxWidth()) {
                CustomInputField(
                    value = birthDateDisplay,
                    onValueChange = { },
                    placeholder = "",
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, null, tint = Color.Gray) }
                )
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }

            LabelText("Пол")
            AppDropdownMenu(
                options = listOf("Мужской", "Женский"),
                selectedOption = selectedGender,
                onOptionSelected = { selectedGender = it }
            )

            LabelText("Вес")
            CustomInputField(value = weight, onValueChange = { weight = it }, placeholder = "0 кг")

            Spacer(Modifier.height(24.dp))

            // Блок фотографии (миниатюра + кнопки)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = NewBgColor
                ) {
                    AsyncImage(
                        model = pet.photoUrl?.takeIf { it.isNotBlank() } ?: R.drawable.no_photo_pet,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Row(
                        modifier = Modifier.clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cat), // иконка скрепки/изменения
                            contentDescription = null,
                            tint = NewPrimaryDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Изменить фотографию",
                            color = NewPrimaryDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Text(
                        text = "Удалить",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 28.dp, top = 4.dp)
                            .clickable { /* Логика удаления фото */ }
                    )
                }
            }

            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp), color = NewPrimaryDark)
            }

            Spacer(Modifier.height(40.dp))

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text("Отменить", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val updatedPet = pet.copy(
                            name = name,
                            breed = breed,
                            weight = weight.toDoubleOrNull() ?: pet.weight,
                            birthDate = birthDateApi // Отправляем ISO формат
                        )
                        onSave(updatedPet)
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark)
                ) {
                    Text("Сохранить", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetProfileEditCardPreview() {
    val samplePet = Pet(
        id = 1,
        name = "Соня",
        breed = "Бернский зенненхунд",
        weight = 4.0,
        birthDate = "05.11.2014",
        photoUrl = ""
    )

    // Обертка, так как BottomSheet требует наличие темы
    MaterialTheme {
        PetProfileEditCard(
            pet = samplePet,
            onBack = {},
            onSave = {},
            onUploadPhoto = {},
            isUploading = false
        )
    }
}