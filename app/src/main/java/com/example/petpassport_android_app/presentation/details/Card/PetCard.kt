package com.example.petpassport_android_app.presentation.details.Card

import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.background
import com.example.petpassport_android_app.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.screens.home.NewPrimaryDark
import com.example.petpassport_android_app.presentation.theme.AppColors
import java.time.LocalDate

@Composable
fun PetCard(
    pet: Pet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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





@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun PetCardPreview() {
    PetCard(
        pet = Pet(
            id = 1,
            name = "Бублик",
            breed = "Корги",
            weight = 12.4,
            birthDate = "",
            photoUrl = "https://images.unsplash.com/photo-1612536056525-7d4ed8a0de4e?w=400",
        )
    )
    {}
}


