package com.apex.arena.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    val id: String,
    val userId: String,
    val depositBalance: Double = 0.0,
    val winningBalance: Double = 0.0,
    val bonusBalance: Double = 50.0,
    val totalBalance: Double = depositBalance + winningBalance + bonusBalance,
    val kycStatus: String = "VERIFIED",
    val upiId: String? = "striker@okaxis"
)

@Serializable
data class WalletTransaction(
    val id: String,
    val amount: Double,
    val type: String, // DEPOSIT, WITHDRAWAL, ENTRY_FEE, PRIZE_PAYOUT, BONUS_REWARD
    val status: String, // SUCCESS, PENDING, FAILED
    val referenceId: String,
    val description: String,
    val createdAt: String
)
