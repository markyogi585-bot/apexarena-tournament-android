package com.apex.arena.domain.repository

import com.apex.arena.domain.models.Wallet
import com.apex.arena.domain.models.WalletTransaction
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getWallet(userId: String): Flow<Wallet>
    fun getTransactions(userId: String): Flow<List<WalletTransaction>>
    suspend fun addDeposit(userId: String, amount: Double, upiId: String): Result<Wallet>
    suspend fun requestWithdrawal(userId: String, amount: Double, upiId: String): Result<Unit>
}
