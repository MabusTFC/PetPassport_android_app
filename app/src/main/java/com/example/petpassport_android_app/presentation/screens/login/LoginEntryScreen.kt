package com.example.petpassport_android_app.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.TopBarCard
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.presentation.details.Card.TextFieldCard

@Composable
fun LoginEntryContent(
    state: LoginScreenModel.State,
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NewBgColor)
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_bar_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.3f))

            // Заголовок экрана
            Text(
                text = "Войти в аккаунт",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = NewPrimaryDark
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Белая карточка с формой
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldCard(value = login, onValueChange = onLoginChange, text = "Логин")
                    TextFieldCard(value = password, onValueChange = onPasswordChange, text = "Пароль")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (state is LoginScreenModel.State.Error) {
                        ErrorCard(message = state.message)
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                        shape = RoundedCornerShape(12.dp),
                        enabled = state !is LoginScreenModel.State.Loading
                    ) {
                        Text("Войти", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    TextButton(onClick = { /* Логика восстановления */ }) {
                        Text(
                            text = "Забыли пароль?",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}



@Preview
@Composable
fun LoginEntryPreview() {
    LoginEntryContent(
        state = LoginScreenModel.State.Idle,
        login = "PetUser",
        onLoginChange = {},
        password = "password",
        onPasswordChange = {},
        onBack = {},
        onSubmit = {}
    )
}