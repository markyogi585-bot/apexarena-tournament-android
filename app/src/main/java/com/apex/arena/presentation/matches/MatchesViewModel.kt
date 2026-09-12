package com.apex.arena.presentation.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.arena.domain.models.Match
import com.apex.arena.domain.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MatchesUiState(
    val isLoading: Boolean = true,
    val selectedTab: Int = 0, // 0 = Upcoming/Live, 1 = Completed
    val matches: List<Match> = emptyList(),
    val selectedMatch: Match? = null,
    val errorMessage: String? = null
)

class MatchesViewModel(
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        loadMatches()
    }

    fun loadMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            matchRepository.getMyMatches("user_demo_01").collect { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    matches = list
                )
            }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun loadMatchDetail(matchId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = matchRepository.getMatchById(matchId)
            result.onSuccess { match ->
                _uiState.value = _uiState.value.copy(isLoading = false, selectedMatch = match)
            }
        }
    }
}
