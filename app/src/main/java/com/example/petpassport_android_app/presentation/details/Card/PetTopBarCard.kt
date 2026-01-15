package com.example.petpassport_android_app.presentation.details.Card

import com.example.petpassport_android_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetTopBar(
    petName: String = "Соня",
    onBackClick: () -> Unit = {},
    onProceduresClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Pet logo",
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = petName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        },
        actions = {
            // Две "вкладки" справа как кнопки
            TextButton(onClick = onProceduresClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_spritz),
                    contentDescription = "",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Предстоящие процедуры")
            }

            TextButton(onClick = onHistoryClick) {
                Image(
                    painter = painterResource(id = R.drawable.medical_folder),
                    contentDescription = "",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Медицинская история")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}



