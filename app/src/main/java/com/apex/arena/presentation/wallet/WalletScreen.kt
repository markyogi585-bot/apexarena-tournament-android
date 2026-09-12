package com.apex.arena.presentation.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexAccentButton
import com.apex.arena.core.components.ApexOutlinedButton
import com.apex.arena.core.components.ApexPrimaryButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.theme.AmberWarning
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.CrimsonError
import com.apex.arena.core.theme.CyanGlow
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.ElevatedSurface
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.MutedSlate
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.RadiantRose
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val wallet = uiState.wallet

    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val withdrawSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (uiState.isLoading || wallet == null) {
        LoadingStateView(message = "Synchronizing Secure Ledger...")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Gladiator Wallet & Passbook",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.spaceM)
        ) {
            // Success or Error Banner
            item {
                if (uiState.successMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(1.dp, EmeraldSuccess, RoundedCornerShape(Dimensions.buttonRadius)),
                        colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = uiState.successMessage ?: "", color = TextCrisp, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // High-End CRED / Navi Grade Metallic Card
            item {
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            Brush.horizontalGradient(listOf(NeonViolet, RadiantRose, CyanGlow)),
                            RoundedCornerShape(Dimensions.cardRadius)
                        ),
                    shape = RoundedCornerShape(Dimensions.cardRadius),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface)
                ) {
                    Column(modifier = Modifier.padding(Dimensions.spaceL)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL ARENA BALANCE",
                                color = SoftLilac,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            IconButton(onClick = { viewModel.toggleBalanceVisibility() }) {
                                Icon(
                                    imageVector = if (uiState.isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Balance",
                                    tint = TextMuted
                                )
                            }
                        }

                        Text(
                            text = if (uiState.isBalanceVisible) "₹${wallet.totalBalance.toInt()}" else "₹ ••••••",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = TextCrisp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spaceM))

                        // Balance Breakdown Sub-Pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MidnightCard, RoundedCornerShape(Dimensions.buttonRadius))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "UNLOCKED WINNINGS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (uiState.isBalanceVisible) "₹${wallet.winningBalance.toInt()}" else "₹•••",
                                    color = EmeraldSuccess,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Column {
                                Text(text = "DEPOSITS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (uiState.isBalanceVisible) "₹${wallet.depositBalance.toInt()}" else "₹•••",
                                    color = TextCrisp,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(text = "BONUS CASH", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (uiState.isBalanceVisible) "₹${wallet.bonusBalance.toInt()}" else "₹•••",
                                    color = AmberWarning,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimensions.spaceL))

                        // Quick Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ApexPrimaryButton(
                                text = "+ ADD CASH",
                                onClick = { viewModel.openAddMoneySheet() },
                                modifier = Modifier.weight(1f)
                            )
                            ApexAccentButton(
                                text = "WITHDRAW",
                                onClick = { viewModel.openWithdrawSheet() },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.spaceL))
            }

            // Transaction History Passbook Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT PASSBOOK TRANSACTIONS",
                        color = TextCrisp,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "UPI Verified",
                        color = EmeraldSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
            }

            items(uiState.transactions) { tx ->
                val isCredit = tx.type in listOf("DEPOSIT", "PRIZE_PAYOUT", "BONUS_REWARD")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, BorderViolet.copy(alpha = 0.5f), RoundedCornerShape(Dimensions.cardRadius)),
                    shape = RoundedCornerShape(Dimensions.cardRadius),
                    colors = CardDefaults.cardColors(containerColor = MidnightCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.spaceM),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isCredit) EmeraldSuccess.copy(alpha = 0.15f) else CrimsonError.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = if (isCredit) EmeraldSuccess else CrimsonError,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(Dimensions.spaceM))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.description,
                                color = TextCrisp,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${tx.createdAt} • Ref: ${tx.referenceId}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = if (isCredit) "+₹${tx.amount.toInt()}" else "-₹${tx.amount.toInt()}",
                            color = if (isCredit) EmeraldSuccess else CrimsonError,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimensions.spaceXL))
            }
        }
    }

    // Modal Bottom Sheet for Instant Add Cash
    if (uiState.isAddMoneySheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeAddMoneySheet() },
            sheetState = addSheetState,
            containerColor = MidnightCard,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .background(TextMuted.copy(alpha = 0.4f), CircleShape)
                )
            }
        ) {
            AddMoneyBottomSheetContent(
                onAddMoney = { amt, upi -> viewModel.depositMoney(amt, upi) },
                onCancel = { viewModel.closeAddMoneySheet() }
            )
        }
    }

    // Modal Bottom Sheet for Instant Withdrawal
    if (uiState.isWithdrawSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeWithdrawSheet() },
            sheetState = withdrawSheetState,
            containerColor = MidnightCard,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .background(TextMuted.copy(alpha = 0.4f), CircleShape)
                )
            }
        ) {
            WithdrawBottomSheetContent(
                maxWithdrawable = wallet.winningBalance,
                onWithdraw = { amt, upi -> viewModel.withdrawMoney(amt, upi) },
                onCancel = { viewModel.closeWithdrawSheet() }
            )
        }
    }
}

@Composable
fun AddMoneyBottomSheetContent(
    onAddMoney: (Double, String) -> Unit,
    onCancel: () -> Unit
) {
    var amountText by remember { mutableStateOf("100") }
    var upiId by remember { mutableStateOf("gladiator@okaxis") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.spaceL)
    ) {
        Text(
            text = "ADD CASH TO WALLET",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = TextCrisp
        )
        Text(
            text = "Instant 100% Secure UPI Gateway Deposit",
            color = TextMuted,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        // Quick Amount Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("50", "100", "250", "500").forEach { chipAmount ->
                val isSelected = amountText == chipAmount
                FilterChip(
                    selected = isSelected,
                    onClick = { amountText = chipAmount },
                    label = { Text("₹$chipAmount", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = TextCrisp,
                        containerColor = MutedSlate,
                        labelColor = SoftLilac
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        ApexTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = "Enter Amount (₹)",
            leadingIcon = Icons.Default.CurrencyRupee,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        ApexTextField(
            value = upiId,
            onValueChange = { upiId = it },
            label = "Your UPI ID (VPA)",
            placeholder = "mobile@upi / username@okhdfcbank"
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceL))

        ApexPrimaryButton(
            text = "PAY ₹${amountText.ifBlank { "0" }} VIA UPI INSTANT",
            onClick = {
                val amt = amountText.toDoubleOrNull() ?: 50.0
                onAddMoney(amt, upiId)
            }
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceL))
    }
}

@Composable
fun WithdrawBottomSheetContent(
    maxWithdrawable: Double,
    onWithdraw: (Double, String) -> Unit,
    onCancel: () -> Unit
) {
    var amountText by remember { mutableStateOf("${maxWithdrawable.toInt().coerceAtLeast(50)}") }
    var upiId by remember { mutableStateOf("gladiator@okaxis") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.spaceL)
    ) {
        Text(
            text = "INSTANT UPI WITHDRAWAL",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = TextCrisp
        )
        Text(
            text = "Max Withdrawable Winnings: ₹${maxWithdrawable.toInt()}",
            color = EmeraldSuccess,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        ApexTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = "Withdrawal Amount (₹)",
            leadingIcon = Icons.Default.CurrencyRupee,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        ApexTextField(
            value = upiId,
            onValueChange = { upiId = it },
            label = "Receiving UPI ID",
            placeholder = "yourname@oksbi"
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceL))

        ApexAccentButton(
            text = "INSTANT TRANSFER TO UPI",
            onClick = {
                val amt = amountText.toDoubleOrNull() ?: 50.0
                onWithdraw(amt, upiId)
            }
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceL))
    }
}
