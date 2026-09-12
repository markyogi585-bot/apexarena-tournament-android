package com.apex.arena.presentation.tournaments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.arena.domain.models.Match
import com.apex.arena.domain.models.Tournament
import com.apex.arena.domain.repository.MatchRepository
import com.apex.arena.domain.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TournamentUiState(
    val isLoading: Boolean = true,
    val tournaments: List<Tournament> = emptyList(),
    val selectedTournament: Tournament? = null,
    val bracketMatches: List<Match> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String? = null,
    val isRegistered: Boolean = false,
    val errorMessage: String? = null
)

class TournamentViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TournamentUiState())
    val uiState: StateFlow<TournamentUiState> = _uiState.asStateFlow()

    init {
        loadTournaments()
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            tournamentRepository.getTournaments(
                searchQuery = _uiState.value.searchQuery,
                filterFormat = _uiState.value.selectedFilter
            ).collect { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tournaments = list
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadTournaments()
    }

    fun onFilterChange(format: String?) {
        _uiState.value = _uiState.value.copy(selectedFilter = format)
        loadTournaments()
    }

    fun loadTournamentDetails(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = tournamentRepository.getTournamentById(id)
            result.onSuccess { tournament ->
                matchRepository.getTournamentBracket(id).collect { bracket ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        selectedTournament = tournament,
                        bracketMatches = bracket
                    )
                }
            }
        }
    }

    fun registerCurrentPlayer(tournamentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = tournamentRepository.registerForTournament(tournamentId, "user_demo_01")
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isRegistered = true)
            }
        }
    }
}
