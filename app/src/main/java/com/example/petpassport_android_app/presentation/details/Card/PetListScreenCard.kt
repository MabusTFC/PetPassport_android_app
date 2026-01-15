package com.example.petpassport_android_app.presentation.details.Card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.presentation.theme.AppColors.Card

@Composable
fun PetCardLoading() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Text("Загрузка...", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PetCardError(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ошибка: $message")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}