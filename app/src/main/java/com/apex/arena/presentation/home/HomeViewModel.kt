package com.apex.arena.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.arena.domain.models.Match
import com.apex.arena.domain.models.Profile
import com.apex.arena.domain.models.Tournament
import com.apex.arena.domain.repository.LeaderboardRepository
import com.apex.arena.domain.repository.MatchRepository
import com.apex.arena.domain.repository.ProfileRepository
import com.apex.arena.domain.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val featuredTournaments: List<Tournament> = emptyList(),
    val liveMatches: List<Match> = emptyList(),
    val topRankers: List<Profile> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository,
    private val profileRepository: ProfileRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val profileResult = profileRepository.getProfile("user_demo_01")
                val profile = profileResult.getOrNull()

                tournamentRepository.getTournaments().collect { tournaments ->
                    matchRepository.getMyMatches("user_demo_01").collect { matches ->
                        leaderboardRepository.getTopPlayers().collect { rankers ->
                            _uiState.value = HomeUiState(
                                isLoading = false,
                                profile = profile,
                                featuredTournaments = tournaments,
                                liveMatches = matches.filter { it.status == "LIVE" || it.status == "SCHEDULED" },
                                topRankers = rankers.take(3)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load dashboard data."
                )
            }
        }
    }
}
