package com.apex.arena.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.arena.domain.models.Profile
import com.apex.arena.domain.repository.AuthRepository
import com.apex.arena.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val isUpdated: Boolean = false
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = profileRepository.getProfile("user_demo_01")
            result.onSuccess { profile ->
                _uiState.value = ProfileUiState(isLoading = false, profile = profile)
            }
        }
    }

    fun updateProfile(displayName: String, gameId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val current = _uiState.value.profile ?: return@launch
            val updated = current.copy(displayName = displayName, gameId = gameId)
            val result = profileRepository.updateProfile(updated)
            result.onSuccess {
                _uiState.value = ProfileUiState(isLoading = false, profile = it, isUpdated = true)
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
