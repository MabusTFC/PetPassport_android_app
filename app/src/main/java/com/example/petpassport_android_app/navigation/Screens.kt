package com.example.petpassport_android_app.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenContent
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenModel
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenContent
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenModel

class LoginNavigationScreen(): Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<LoginScreenModel>()
        val state by model.state.collectAsState()

        LoginScreenContent(
            state = state,
            onLoginClick = {
                model.onLoginClicked(
                    openTelegram = { link ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    },
                    navigator = navigator
                )
            }
        )
    }
}

class PetListNavigationScreen(): Screen{
    @Composable
    override fun Content() {
        val model = getScreenModel<PetListScreenModel>()
        val state by model.state.collectAsState()

        PetListScreenContent(
            state = state,
            onRetry = { model.retry() }
        )
    }

}