package com.example.petpassport_android_app.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun PetListScreenContent(
    state: PetListScreenModel.PetsState,
    onRetry: () -> Unit
){

}

@Preview
@Composable
fun PetListScreenPreview() {
    PetListScreenContent(
        state = PetListScreenModel.PetsState.Empty,
        onRetry = {}
    )
}