package com.example.petpassport_android_app.presentation.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.LocalNavigator


@Composable
fun SettingsScreen() {
    val navigator = LocalNavigator.current
    SettingsScreenContent(
        onGoToBackClick = {
            navigator?.pop()
        }
    )
}


@Composable
fun SettingsScreenContent(
    onGoToBackClick: () -> Unit,
) {
    Scaffold { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize()
                .background(Color.Yellow)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Button(
                    onClick = {
                        onGoToBackClick()
                    }
                ) {
                    Text("Go back")
                }
            }
        }
    }
}


@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreenContent(onGoToBackClick = {})
}