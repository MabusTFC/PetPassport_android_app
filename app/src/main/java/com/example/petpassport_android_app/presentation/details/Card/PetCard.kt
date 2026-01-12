package com.example.petpassport_android_app.presentation.details.Card

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetCard(
    pet: Pet,
    onClick: () -> Unit
)  {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = pet.photoUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .size(72.dp)

            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = pet.name,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
                Text("Порода: ${pet.breed}")
                Text("Вес: ${pet.weight} кг")
                Text("Дата рождения: ${pet.birthDate}")
            }
        }
    }
}
