package com.example.petpassport_android_app.presentation.details.Card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun RoundedRectangleCard(
    text: String,
    fontSize: Int = 11,
    horizontal: Int = 6,
    vertical: Int = 2
) {
    Box(
        modifier = Modifier
            .background(
                color = AppColors.Primary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = horizontal.dp, vertical = vertical.dp)
    ) {
        Text(
            text = text,
            color = AppColors.Primary,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = fontSize.sp
        )
    }
}