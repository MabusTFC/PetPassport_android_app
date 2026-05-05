package com.example.petpassport_android_app.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreenContent(
    login: String,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Профиль",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NewPrimaryDark,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Divider(color = Color(0xFFF0F0F0))

            Spacer(Modifier.height(20.dp))

            // Логин
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Логин",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = login,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewPrimaryDark
                )
            }

            Spacer(Modifier.height(20.dp))

            // Электронная почта — заглушка
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Электронная почта",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Не указана",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewPrimaryDark
                )
            }

            Spacer(Modifier.height(32.dp))

            // Кнопка изменить пароль
            Button(
                onClick = onChangePassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Изменить пароль", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))

            // Кнопка выйти из аккаунта
            OutlinedButton(
                onClick = {
                    onDismiss()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFE53935)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE53935))
            ) {
                Text("Выйти из аккаунта", fontWeight = FontWeight.Bold)
            }
        }
    }
}