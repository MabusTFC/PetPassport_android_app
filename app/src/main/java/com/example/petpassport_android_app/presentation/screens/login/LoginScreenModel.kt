package com.example.petpassport_android_app.presentation.screens.login

import android.content.SharedPreferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.example.petpassport_android_app.navigation.LoginNavigationScreen
import com.example.petpassport_android_app.navigation.PetListNavigationScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginScreenModel @Inject constructor(
    private val authRepository: AuthRepository
) : ScreenModel {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Success : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    // ← вызывается при старте приложения — проверяем есть ли сохранённый токен
    fun checkAuth(navigator: Navigator) {
        screenModelScope.launch {
            if (authRepository.isLoggedIn()) {
                navigator.replace(PetListNavigationScreen())
            }
        }
    }

    fun login(login: String, password: String, navigator: Navigator) {
        screenModelScope.launch {
            val cleanLogin = login.trim()
            if (cleanLogin.isBlank() || password.isBlank()) {
                _state.value = State.Error("Введите логин и пароль")
                return@launch
            }
            _state.value = State.Loading
            try {
                val owner = authRepository.login(cleanLogin, password)
                if (owner != null) {
                    // ← токены сохраняются внутри AuthRepositoryImpl автоматически
                    _state.value = State.Success
                    navigator.push(PetListNavigationScreen())
                } else {
                    _state.value = State.Error("Неверный логин или пароль")
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка соединения: ${e.message}")
            }
        }
    }

    fun register(login: String, password: String, navigator: Navigator) {
        screenModelScope.launch {
            val cleanLogin = login.trim()
            if (cleanLogin.isBlank() || password.isBlank()) {
                _state.value = State.Error("Введите логин и пароль")
                return@launch
            }
            _state.value = State.Loading
            try {
                val owner = authRepository.register(cleanLogin, password)
                if (owner != null) {
                    // ← токены сохраняются внутри AuthRepositoryImpl автоматически
                    _state.value = State.Success
                    navigator.push(PetListNavigationScreen())
                } else {
                    _state.value = State.Error("Ошибка регистрации: логин может быть занят")
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка соединения: ${e.message}")
            }
        }
    }

    fun logout(navigator: Navigator) {
        screenModelScope.launch {
            authRepository.logout()
            navigator.replaceAll(LoginNavigationScreen())
        }
    }

    fun resetState() {
        _state.value = State.Idle
    }
}
