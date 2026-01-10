package com.example.petpassport_android_app.presentation.screens.login

import android.content.SharedPreferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.petpassport_android_app.data.dto.AuthStatusDto
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import com.example.petpassport_android_app.navigation.PetListNavigationScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginScreenModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ownerRepository: OwnerRepository,
    private val sharedPreferences: SharedPreferences
) : ScreenModel {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object WaitingForTelegram : State()
        object Success : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    fun onLoginClicked(
        openTelegram: (String) -> Unit,
        navigator: Navigator
    ) {
        screenModelScope.launch {
            try {
                _state.value = State.Loading

                val uuid = authRepository.startLoginSession()
                val link = "https://t.me/MyPetPassportBot?start=$uuid"

                openTelegram(link)

                _state.value = State.WaitingForTelegram
                pollAuthStatus(uuid, navigator)

            } catch (e: Exception) {
                _state.value = State.Error("Ошибка соединения: ${e.message}")
            }
        }
    }

    private suspend fun pollAuthStatus(uuid: String, navigator: Navigator) {
        repeat(30) {
            delay(2000)

            try {
                val result: AuthStatusDto = authRepository.checkLoginStatus(uuid)

                if (result.status == "SUCCESS" && result.telegramId != null) {
                    sharedPreferences.edit()
                        .putString("access_token", result.accessToken)
                        .putString("telegram_id", result.telegramId)
                        .apply()

                    val owner = ownerRepository.getOwnerByTelegramId(result.telegramId)
                    owner?.let {
                        sharedPreferences.edit()
                            .putInt("owner_id", it.id)
                            .apply()
                    }

                    _state.value = State.Success
                    navigator.push(PetListNavigationScreen())
                    return
                }
            } catch (e: Exception) { }
        }

        _state.value = State.Error("Время ожидания истекло")
    }
}