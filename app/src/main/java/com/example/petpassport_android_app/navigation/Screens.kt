package com.example.petpassport_android_app.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.presentation.screens.events.EventsScreenContent
import com.example.petpassport_android_app.presentation.screens.events.EventsScreenModel
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenContent
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenModel
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenContent
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenModel
import com.example.petpassport_android_app.presentation.screens.login.LoginEntryContent
import com.example.petpassport_android_app.presentation.screens.login.RegisterEntryContent
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenContent
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenModel


class LoginNavigationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<LoginScreenModel>()
        val state by model.state.collectAsState()

        // Сброс состояния модели при возврате на главный экран
        LaunchedEffect(Unit) { model.resetState() }

        // Вызываем только UI функцию
        LoginScreenContent(
            state = state,
            onLoginClick = { navigator.push(LoginEntryNavigationScreen()) },
            onRegisterClick = { navigator.push(RegisterEntryNavigationScreen()) }
        )
    }
}

class LoginEntryNavigationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<LoginScreenModel>()
        val state by model.state.collectAsState()

        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Вызываем только UI функцию
        LoginEntryContent(
            state = state,
            login = login,
            onLoginChange = { login = it },
            password = password,
            onPasswordChange = { password = it },
            onBack = { navigator.pop() },
            onSubmit = { model.login(login, password, navigator) }
        )
    }
}

class RegisterEntryNavigationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<LoginScreenModel>()
        val state by model.state.collectAsState()

        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Вызываем только UI функцию
        RegisterEntryContent(
            state = state,
            login = login,
            onLoginChange = { login = it },
            password = password,
            onPasswordChange = { password = it },
            onBack = { navigator.pop() },
            onSubmit = { model.register(login, password, navigator) }
        )
    }
}

class PetListNavigationScreen() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<PetListScreenModel>()
        val state by model.state.collectAsState()
        val petStates by model.petStates.collectAsState()

        LaunchedEffect(Unit) {
            model.retry()
        }

        PetListScreenContent(
            state = state,
            petStates = petStates,
            onRetry = { model.retry() },
            onAddPet = { model.addPet(it) },
            onPetProfile = { petId ->
                navigator.push(PetProfileNavigationScreen(petId))
            },
            onRefreshPet = { petId -> model.refreshPet(petId) },
            onBack = {navigator.pop()}
        )
    }
}




class PetProfileNavigationScreen(
    private val petId: Int
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<PetProfileScreenModel>()
        val state by model.state.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(petId) {
            model.loadPetById(petId)
        }

        PetProfileScreenContent(
            state = state,
            onBack = { navigator.pop() },
            onEditProfile = { model.enableEditMode() },
            onSavePet = { model.savePet(it) },
            onOpenEvents = {
                navigator.push(EventsNavigationScreen(petId))
            },
            onUploadPhoto = { bytes -> model.uploadPhoto(petId, bytes) },
            context = context
        )
    }
}


class EventsNavigationScreen(
    private val petId: Int
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<EventsScreenModel>()
        val state by model.state.collectAsState()

        LaunchedEffect(petId) {
            model.loadEvents(petId)
        }

        EventsScreenContent(
            state = state,
            onBack = { navigator.pop() },
            onAddEvent = { event ->
                when (event) {
                    is Vaccine -> model.addVaccine(event.copy(petId = petId), petId)
                    is Treatment -> model.addTreatment(event.copy(petId = petId), petId)
                    is DoctorVisit -> model.addDoctorVisit(event.copy(petId = petId), petId)
                }
            }
        )
    }
}






