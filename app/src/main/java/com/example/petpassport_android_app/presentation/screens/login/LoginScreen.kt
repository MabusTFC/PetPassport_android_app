package com.example.petpassport_android_app.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.presentation.details.Card.*
import com.example.petpassport_android_app.presentation.details.button.PrimaryButton
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun LoginScreenContent(
    state: LoginScreenModel.State,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(0.dp))

            when (state) {
                is LoginScreenModel.State.Idle -> {
                    PrimaryButton(
                        text = "Войти через Telegram",
                        onClick = onLoginClick
                    )
                }
                is LoginScreenModel.State.Loading -> {
                    LoadingCard("Получаем данные…")
                }
                is LoginScreenModel.State.WaitingForTelegram -> {
                    LoadingCard("Откройте Telegram для авторизации")
                }
                is LoginScreenModel.State.Success -> {
                    SuccessCard()
                }
                is LoginScreenModel.State.Error -> {
                    ErrorCard(message = state.message)
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginScreenModel.State.Idle,
        onLoginClick = {}
    )
}