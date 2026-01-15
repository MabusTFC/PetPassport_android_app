package com.example.petpassport_android_app.presentation.details.Card

import android.R.attr.onClick
import androidx.compose.foundation.background
import com.example.petpassport_android_app.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.theme.AppColors
import java.time.LocalDate

@Composable
fun PetCard(
    pet: Pet,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            // 🐶 Фото
            AsyncImage(
                model = if (pet.photoUrl.isNullOrEmpty())
                    R.drawable.avatar_pet_defualt
                else pet.photoUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 📄 Текстовая часть
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Имя — может переноситься
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(6.dp))

                // Строка с породой справа
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {

                    RoundedRectangleCard(pet.breed)
                }
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


