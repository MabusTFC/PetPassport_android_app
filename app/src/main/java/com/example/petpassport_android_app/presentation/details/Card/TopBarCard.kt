package com.example.petpassport_android_app.presentation.details.Card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.presentation.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCard(
    title: String,
    @DrawableRes iconRes: Int,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .statusBarsPadding()   // ⭐ отступ под системную панель
            .padding(top = 4.dp),  // небольшой дополнительный отступ
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "   |   ",
                    color = AppColors.TextSecondary
                )

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AppColors.TextSecondary
                )

                Text(
                    text = "   $title   ",
                    color = AppColors.TextSecondary
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Назад",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}
