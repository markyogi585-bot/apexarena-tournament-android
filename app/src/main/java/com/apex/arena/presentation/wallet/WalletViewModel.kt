package com.apex.arena.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.arena.domain.models.Wallet
import com.apex.arena.domain.models.WalletTransaction
import com.apex.arena.domain.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WalletUiState(
    val isLoading: Boolean = true,
    val wallet: Wallet? = null,
    val transactions: List<WalletTransaction> = emptyList(),
    val isBalanceVisible: Boolean = true,
    val isAddMoneySheetOpen: Boolean = false,
    val isWithdrawSheetOpen: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class WalletViewModel(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWalletData()
    }

    fun loadWalletData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            walletRepository.getWallet("user_demo_01").collect { wallet ->
                walletRepository.getTransactions("user_demo_01").collect { txs ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        wallet = wallet,
                        transactions = txs
                    )
                }
            }
        }
    }

    fun toggleBalanceVisibility() {
        _uiState.value = _uiState.value.copy(isBalanceVisible = !_uiState.value.isBalanceVisible)
    }

    fun openAddMoneySheet() {
        _uiState.value = _uiState.value.copy(isAddMoneySheetOpen = true, errorMessage = null, successMessage = null)
    }

    fun closeAddMoneySheet() {
        _uiState.value = _uiState.value.copy(isAddMoneySheetOpen = false)
    }

    fun openWithdrawSheet() {
        _uiState.value = _uiState.value.copy(isWithdrawSheetOpen = true, errorMessage = null, successMessage = null)
    }

    fun closeWithdrawSheet() {
        _uiState.value = _uiState.value.copy(isWithdrawSheetOpen = false)
    }

    fun depositMoney(amount: Double, upiId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = walletRepository.addDeposit("user_demo_01", amount, upiId)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAddMoneySheetOpen = false,
                    successMessage = "₹${amount.toInt()} added successfully via UPI!"
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.message)
            }
        }
    }

    fun withdrawMoney(amount: Double, upiId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = walletRepository.requestWithdrawal("user_demo_01", amount, upiId)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isWithdrawSheetOpen = false,
                    successMessage = "₹${amount.toInt()} transferred to $upiId successfully!"
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.message)
            }
        }
    }
}
