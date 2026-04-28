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
import com.example.petpassport_android_app.domain.model.Event.EventReminderUiPayload
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.presentation.screens.eventDetail.EventDetailScreenContent
import com.example.petpassport_android_app.presentation.screens.events.EventsScreenContent
import com.example.petpassport_android_app.presentation.screens.events.EventsScreenModel
import com.example.petpassport_android_app.presentation.screens.events.eventDetails.EventDetailScreenModel
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenContent
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenModel
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenContent
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenModel
import com.example.petpassport_android_app.presentation.screens.login.LoginEntryContent
import com.example.petpassport_android_app.presentation.screens.login.RegisterEntryContent
import com.example.petpassport_android_app.presentation.screens.medicalHistory.MedicalHistoryScreenContent
import com.example.petpassport_android_app.presentation.screens.medicalHistory.MedicalHistoryScreenModel
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenContent
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenModel


class LoginNavigationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<LoginScreenModel>()
        val state by model.state.collectAsState()

        LaunchedEffect(Unit) { model.resetState() }
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
        val isNotificationsEnabled by model.isNotificationsEnabled.collectAsState()
        val context = LocalContext.current

        val currentPetName by rememberUpdatedState(
            when (val s = state) {
                is PetProfileScreenModel.State.View -> s.pet.name
                is PetProfileScreenModel.State.Edit -> s.pet.name
                else -> ""
            }
        )

        LaunchedEffect(petId) {
            model.loadPetById(petId)
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    model.loadPetById(petId)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        PetProfileScreenContent(
            state = state,
            isNotificationsEnabled = isNotificationsEnabled,
            onBack = { navigator.pop() },
            onEditProfile = { model.enableEditMode() },
            onSavePet = { model.savePet(it) },
            onDismissEdit = { model.dismissEditMode() },
            onEventReminderToggle = { ev, enabled ->
                model.onEventReminderToggle(context, ev, enabled)
            },
            onOpenEvents = {
                val petName = when (val s = state) {
                    is PetProfileScreenModel.State.View -> s.pet.name
                    is PetProfileScreenModel.State.Edit -> s.pet.name
                    else -> ""
                }
                navigator.push(EventsNavigationScreen(petId = petId, petName = petName))
            },
            onOpenHistory = {
                val petName = when (val s = state) {
                    is PetProfileScreenModel.State.View -> s.pet.name
                    is PetProfileScreenModel.State.Edit -> s.pet.name
                    else -> ""
                }
                navigator.push(MedicalHistoryNavigationScreen(petId = petId, petName = petName))
            },
            onEventClick = { event ->
                val petName = when (val s = state) {
                    is PetProfileScreenModel.State.View -> s.pet.name
                    is PetProfileScreenModel.State.Edit -> s.pet.name
                    else -> ""
                }
                navigator.push(EventDetailNavigationScreen(event = event, petName = petName))
            },
            onUploadPhoto = { bytes -> model.uploadPhoto(petId, bytes) },
            context = context
        )
    }
}

class EventsNavigationScreen(
    private val petId: Int,
    private val petName: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<EventsScreenModel>()
        val state by model.state.collectAsState()
        val isNotificationsEnabled by model.isNotificationsEnabled.collectAsState()
        val context = LocalContext.current

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, lifecycleEvent ->
                if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
                    model.loadEvents(petId)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        EventsScreenContent(
            state = state,
            petName = petName,
            isNotificationsEnabled = isNotificationsEnabled,
            onBack = { navigator.pop() },
            onEventClick = { event ->
                navigator.push(EventDetailNavigationScreen(event = event, petName = petName))
            },
            onToggleNotifications = { enabled ->
                model.toggleNotifications(context, enabled)
            },
            onEventReminderToggle = { event, enabled ->
                model.onEventReminderToggle(context, event, enabled)
            },
            onAddEvent = { newEvent, reminder ->  // ← теперь два параметра
                when (newEvent) {
                    is Vaccine -> model.addVaccine(context, newEvent.copy(petId = petId), petId, reminder)
                    is Treatment -> model.addTreatment(context, newEvent.copy(petId = petId), petId, reminder)
                    is DoctorVisit -> model.addDoctorVisit(context, newEvent.copy(petId = petId), petId, reminder)
                }
            }
        )
    }
}

class MedicalHistoryNavigationScreen(
    private val petId: Int,
    private val petName: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<MedicalHistoryScreenModel>()
        val state by model.state.collectAsState()
        val isNotificationsEnabled by model.isNotificationsEnabled.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(petId) {
            model.loadHistory(petId)
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    model.loadHistory(petId)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        MedicalHistoryScreenContent(
            state = state,
            petName = petName,
            isNotificationsEnabled = isNotificationsEnabled,
            onBack = { navigator.pop() },
            onEventClick = { event ->
                navigator.push(EventDetailNavigationScreen(event = event, petName = petName))
            },
            onEventReminderToggle = { ev, enabled ->
                model.onEventReminderToggle(context, ev, enabled)
            }
        )
    }
}

class EventDetailNavigationScreen(
    private val event: PetEvent,
    private val petName: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model = getScreenModel<EventDetailScreenModel>()
        val state by model.state.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(state) {
            if (state is EventDetailScreenModel.State.Deleted ||
                state is EventDetailScreenModel.State.Saved) {
                navigator.pop()
            }
        }

        EventDetailScreenContent(
            event = event,
            petName = petName,
            onBack = { navigator.pop() },
            onSave = { updatedEvent -> model.updateEvent(context, updatedEvent) },
            onDelete = { model.deleteEvent(context, event) },
            isLoading = state is EventDetailScreenModel.State.Loading
        )
    }
}








