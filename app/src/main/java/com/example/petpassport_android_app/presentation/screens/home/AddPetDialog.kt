package com.example.petpassport_android_app.presentation.screens.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet
import java.text.SimpleDateFormat
import java.util.*

// Цвета из вашего нового дизайна


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDialog(
    onDismiss: () -> Unit,
    onAdd: (Pet) -> Unit
) {
    // Состояния полей
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var birthDateDisplay by remember { mutableStateOf("01.07.2024") }
    var birthDateApi by remember { mutableStateOf("2024-07-01") }
    var selectedAnimal by remember { mutableStateOf("Кот") }
    var selectedGender by remember { mutableStateOf("Женский") }
    var photoUri by remember { mutableStateOf<android.net.Uri?>(null) }

    // Календарь
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> photoUri = uri }

    // Логика выбора даты
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        // Формат для экрана
                        birthDateDisplay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
                        // Формат для сервера (API)
                        birthDateApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                    }
                    showDatePicker = false
                }) { Text("ОК", color = NewPrimaryDark, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Добавить питомца",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NewPrimaryDark,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
            )

            Divider(color = Color(0xFFF0F0F0))

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

            // РЕАЛИЗАЦИЯ БЛОКА ФОТО
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = NewBgColor
                ) {
                    if (photoUri != null) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.no_photo_pet),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.LightGray
                            )
                        }
                    }
                }
                Spacer(Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_cat), // иконка скрепки или лапки
                    contentDescription = null,
                    tint = NewPrimaryDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Загрузить фотографию",
                    color = NewPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(40.dp))

            // РЕАЛИЗАЦИЯ КНОПОК
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text("Отменить", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onAdd(Pet(0, name, breed, weight.toDoubleOrNull() ?: 0.0, birthDateApi, photoUri?.toString()))
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark)
                ) {
                    Text("Добавить", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ

@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 16.dp),
        color = NewPrimaryDark,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = NewPrimaryDark,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = NewPrimaryDark,
                unfocusedContainerColor = Color(0xFFF8F9FB),
                focusedContainerColor = Color(0xFFF8F9FB)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}