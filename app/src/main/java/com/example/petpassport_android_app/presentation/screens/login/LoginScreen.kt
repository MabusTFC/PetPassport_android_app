package com.example.petpassport_android_app.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.presentation.details.Card.*

// Цвета
val NewBgColor = Color(0xFFF4F5F9)
val NewPrimaryDark = Color(0xFF2E1A7A)
val NewSecondaryText = Color(0xFF4A378B)

@Composable
fun LoginScreenContent(
    state: LoginScreenModel.State,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // Используем Box, чтобы иметь возможность прижать элементы к разным краям
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NewBgColor)
            .statusBarsPadding() // Отступ сверху для челки/статус-бара
            .navigationBarsPadding() // ГАРАНТИРУЕТ, что системные кнопки не перекроют контент
    ) {

        // --- СЛОЙ 1: ЦЕНТРАЛЬНЫЙ БЛОК ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center, // Центрируем всё внутри
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Текст Приветствия
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Привет!",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewSecondaryText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Рады видеть Вас в PetPassport!\nПозаботьтесь о своём любимом питомце.",
                    fontSize = 16.sp,
                    color = NewSecondaryText,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Сетка животных
            Image(
                painter = painterResource(id = R.drawable.bg_animals_grid),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1.15f)
                    .alpha(0.35f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка Войти
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Войти", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        // --- СЛОЙ 2: НИЖНИЙ БЛОК (ЗАРЕГИСТРИРОВАТЬСЯ) ---
        // Box с Alignment.BottomCenter прижимает содержимое к низу
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp), // Дополнительный небольшой отступ от края
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Нет аккаунта? ")
                    withStyle(style = SpanStyle(
                        color = NewPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append("Зарегистрироваться")
                    }
                },
                modifier = Modifier.clickable { onRegisterClick() },
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginScreenModel.State.Idle,
        onLoginClick = {},
        onRegisterClick = {}
    )
}