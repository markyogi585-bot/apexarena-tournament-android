package com.apex.arena.data.repository

import com.apex.arena.domain.models.Wallet
import com.apex.arena.domain.models.WalletTransaction
import com.apex.arena.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class WalletRepositoryImpl : WalletRepository {
    private var currentWallet = Wallet(
        id = "w1",
        userId = "user_demo_01",
        depositBalance = 350.0,
        winningBalance = 1200.0,
        bonusBalance = 100.0
    )

    private val _walletFlow = MutableStateFlow(currentWallet)
    private val _transactionsFlow = MutableStateFlow(
        listOf(
            WalletTransaction("tx1", 500.0, "PRIZE_PAYOUT", "SUCCESS", "UTR983174981", "1st Place: Free Fire Pro Clash", "Today, 4:30 PM"),
            WalletTransaction("tx2", 100.0, "ENTRY_FEE", "SUCCESS", "ORD812938102", "Entry: Valorant Spike Rush", "Yesterday, 6:00 PM"),
            WalletTransaction("tx3", 200.0, "DEPOSIT", "SUCCESS", "UPI819284719", "Instant UPI Deposit (GPay)", "10 Sep, 2:15 PM"),
            WalletTransaction("tx4", 50.0, "BONUS_REWARD", "SUCCESS", "BONUS_SIGNUP", "Gladiator Welcome Bonus", "08 Sep, 10:00 AM")
        )
    )

    override fun getWallet(userId: String): Flow<Wallet> = _walletFlow.asStateFlow()

    override fun getTransactions(userId: String): Flow<List<WalletTransaction>> = _transactionsFlow.asStateFlow()

    override suspend fun addDeposit(userId: String, amount: Double, upiId: String): Result<Wallet> {
        currentWallet = currentWallet.copy(
            depositBalance = currentWallet.depositBalance + amount
        )
        _walletFlow.value = currentWallet

        val newTx = WalletTransaction(
            id = UUID.randomUUID().toString(),
            amount = amount,
            type = "DEPOSIT",
            status = "SUCCESS",
            referenceId = "UPI" + (10000000..99999999).random(),
            description = "Instant UPI Deposit ($upiId)",
            createdAt = "Just now"
        )
        _transactionsFlow.value = listOf(newTx) + _transactionsFlow.value
        return Result.success(currentWallet)
    }

    override suspend fun requestWithdrawal(userId: String, amount: Double, upiId: String): Result<Unit> {
        if (amount > currentWallet.winningBalance) {
            return Result.failure(IllegalArgumentException("Insufficient winning balance. Only winnings can be withdrawn."))
        }
        currentWallet = currentWallet.copy(
            winningBalance = currentWallet.winningBalance - amount
        )
        _walletFlow.value = currentWallet

        val newTx = WalletTransaction(
            id = UUID.randomUUID().toString(),
            amount = amount,
            type = "WITHDRAWAL",
            status = "SUCCESS",
            referenceId = "WDR" + (10000000..99999999).random(),
            description = "Instant Payout to $upiId",
            createdAt = "Just now"
        )
        _transactionsFlow.value = listOf(newTx) + _transactionsFlow.value
        return Result.success(Unit)
    }
}
