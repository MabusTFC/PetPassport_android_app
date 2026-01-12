package com.example.petpassport_android_app.presentation.screens.login

import android.content.SharedPreferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.example.petpassport_android_app.navigation.PetListNavigationScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginScreenModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
) : ScreenModel {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Success : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    fun login(login: String, password: String, navigator: Navigator) {
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val owner = authRepository.login(login, password)
                if (owner != null) {
                    saveOwnerData(owner, password)
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
            _state.value = State.Loading
            try {
                val owner = authRepository.register(login, password)
                if (owner != null) {
                    saveOwnerData(owner, password)
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

    private fun saveOwnerData(owner: Owner, password: String) {
        sharedPreferences.edit()
            .putInt("owner_id", owner.id ?: -1)
            .putString("login", owner.login)
            .putString("password", password)
            .putString("telegram_id", owner.telegramId)
            .apply()
    }

    fun resetState() {
        _state.value = State.Idle
    }
}
