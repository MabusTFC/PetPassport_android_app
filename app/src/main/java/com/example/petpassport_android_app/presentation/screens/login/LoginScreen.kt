package com.example.petpassport_android_app.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = login,
                onValueChange = onLoginChange,
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is LoginScreenModel.State.Idle -> {
                    PrimaryButton(text = "Войти", onClick = onLoginClick)
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryButton(text = "Зарегистрироваться", onClick = onRegisterClick)
                }
                is LoginScreenModel.State.Loading -> LoadingCard("Подождите…")
                is LoginScreenModel.State.Success -> SuccessCard()
                is LoginScreenModel.State.Error -> ErrorCard(message = state.message)
            }
        }
    }
}


@Preview()
@Composable
fun LoginScreenPreview() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LoginScreenContent(
        state = LoginScreenModel.State.Idle,
        login = login,
        onLoginChange = { login = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = {},
        onRegisterClick = {}
    )
}
