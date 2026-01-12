package com.example.petpassport_android_app.presentation.details.Card


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun EmptyPetsState() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        //Image(
        //    painter = painterResource(id = R.drawable.background_empty_pets),
        //    contentDescription = null,
        //    modifier = Modifier.fillMaxSize(),
        //    contentScale = ContentScale.Crop
        //)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_sad_dog),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            //Spacer(modifier = Modifier.height(-5.dp))

            Text(
                text = "У вас пока нет питомцев",
                color = AppColors.Primary,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp, // или 26.sp, 28.sp для еще крупнее
                    fontWeight = FontWeight.Bold, // или FontWeight.ExtraBold
                    color = AppColors.Primary
                ),

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Добавьте первого питомца,\nчтобы создать его паспорт 🐾",
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
