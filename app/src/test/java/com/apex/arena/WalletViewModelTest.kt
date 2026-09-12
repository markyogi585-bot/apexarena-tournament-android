package com.apex.arena

import com.apex.arena.data.repository.WalletRepositoryImpl
import com.apex.arena.presentation.wallet.WalletViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var walletRepository: WalletRepositoryImpl
    private lateinit var viewModel: WalletViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        walletRepository = WalletRepositoryImpl()
        viewModel = WalletViewModel(walletRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadWalletData_loadsNonZeroBalances() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.wallet)
        assertTrue(state.wallet!!.totalBalance > 0)
        assertTrue(state.transactions.isNotEmpty())
    }

    @Test
    fun depositMoney_increasesDepositBalanceAndRecordsTransaction() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val initialBalance = viewModel.uiState.value.wallet!!.totalBalance

        viewModel.depositMoney(200.0, "gladiator@okaxis")
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedBalance = viewModel.uiState.value.wallet!!.totalBalance
        assertEquals(initialBalance + 200.0, updatedBalance, 0.01)
        assertTrue(viewModel.uiState.value.transactions.any { it.type == "DEPOSIT" && it.amount == 200.0 })
    }

    @Test
    fun withdrawMoney_withinWinningBalance_succeeds() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.withdrawMoney(100.0, "gladiator@okaxis")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.successMessage)
    }
}
