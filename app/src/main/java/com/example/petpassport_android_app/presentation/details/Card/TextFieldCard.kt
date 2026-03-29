package com.example.petpassport_android_app.presentation.details.Card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun TextFieldCard(
    value: String,
    onValueChange: (String) -> Unit,
    text: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        // placeholder — текст внутри, который исчезает при начале ввода
        placeholder = {
            Text(text = text, color = Color.Gray)
        },
        // Мы НЕ используем label, чтобы текст не улетал в рамку
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            // Фон белый
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,

            // Контур (рамка)
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color(0xFFD1D1D1),

            // Текст, который вводит пользователь
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            // Цвет курсора
            cursorColor = Color.Gray
        ),
        singleLine = true
    )
}