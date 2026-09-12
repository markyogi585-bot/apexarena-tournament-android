package com.apex.arena.presentation.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.theme.*

@Composable
fun AddCashScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("500") }
    var selectedMethod by remember { mutableStateOf("Instant UPI / QR") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("Add Cash to Wallet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            ApexTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Deposit Amount (₹)",
                leadingIcon = Icons.Default.CurrencyRupee,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("100", "250", "500", "1000", "2000").forEach { amt ->
                    FilterChip(
                        selected = amount == amt,
                        onClick = { amount = amt },
                        label = { Text("+₹$amt") }
                    )
                }
            }

            Text("Select Payment Gateway", style = MaterialTheme.typography.labelLarge, color = ArenaTextSecondary)
            listOf("Instant UPI / QR (PhonePe / GPay / Paytm)", "Credit / Debit Card (Visa / Mastercard)", "Net Banking (SBI / HDFC / ICICI)").forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedMethod == method) ArenaPurplePrimary.copy(alpha = 0.2f) else ArenaDarkSurfaceElevated)
                        .clickable { selectedMethod = method }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedMethod == method,
                        onClick = { selectedMethod = method },
                        colors = RadioButtonDefaults.colors(selectedColor = ArenaPurplePrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(method, color = ArenaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        ApexButton(
            text = "Pay Securely ₹${amount.ifBlank { "0" }}",
            onClick = {
                val amtVal = amount.toDoubleOrNull() ?: 0.0
                if (amtVal > 0) onPaymentSuccess(amtVal)
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Lock
        )
    }
}

@Composable
fun WithdrawScreen(
    onNavigateBack: () -> Unit,
    onWithdrawSuccess: () -> Unit
) {
    var amount by remember { mutableStateOf("1000") }
    var upiId by remember { mutableStateOf("apexuser@okaxis") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("Instant Bank / UPI Payout", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Available Payout Balance", color = ArenaTextSecondary, fontSize = 12.sp)
                        Text("₹1,650", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ArenaGreenSuccess)
                    }
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ArenaGreenSuccess)
                }
            }

            ApexTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Withdrawal Amount (₹)",
                leadingIcon = Icons.Default.CurrencyRupee,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ApexTextField(
                value = upiId,
                onValueChange = { upiId = it },
                label = "Verified UPI VPA / Account",
                leadingIcon = Icons.Default.AccountBalance
            )
        }

        ApexButton(
            text = "Confirm Instant Withdrawal",
            onClick = onWithdrawSuccess,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.AccountBalanceWallet
        )
    }
}

@Composable
fun KycVerificationScreen(
    onNavigateBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("Apex Shadow") }
    var docType by remember { mutableStateOf("PAN") }
    var docNumber by remember { mutableStateOf("ABCDE1234F") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("KYC Identity Verification", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            Text("Required by Indian RBI regulations for instant bank withdrawals over ₹1,000.", color = ArenaTextSecondary, fontSize = 13.sp)

            ApexTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Legal Name (as on Govt ID)")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("PAN", "AADHAAR", "PASSPORT").forEach { dt ->
                    FilterChip(selected = docType == dt, onClick = { docType = dt }, label = { Text(dt) })
                }
            }

            ApexTextField(value = docNumber, onValueChange = { docNumber = it }, label = "$docType Identification Number")
        }

        ApexButton(
            text = "Submit KYC for Verification",
            onClick = onSubmitSuccess,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Security
        )
    }
}

@Composable
fun TransactionHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val txs = listOf(
        Triple("tx-1", "Tournament Entry - BGMI Pro", "-₹150"),
        Triple("tx-2", "Match Prize Won - FF Knockout", "+₹500"),
        Triple("tx-3", "UPI Deposit #88912", "+₹1,000"),
        Triple("tx-4", "Daily Streak Bonus", "+₹50")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            Text("Ledger Passbook & History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(txs) { (id, desc, amt) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetail(id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(desc, fontWeight = FontWeight.SemiBold, color = ArenaTextPrimary, fontSize = 14.sp)
                            Text("ID: #$id • Today", color = ArenaTextMuted, fontSize = 11.sp)
                        }
                        Text(
                            amt,
                            fontWeight = FontWeight.Bold,
                            color = if (amt.startsWith("+")) ArenaGreenSuccess else ArenaAccentRose,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionDetailScreen(
    transactionId: String,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("Transaction Receipt", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("TRANSACTION ID", color = ArenaTextMuted, fontSize = 11.sp)
                    Text("#$transactionId-APEX-UTR-2026", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                    HorizontalDivider(color = ArenaBorder)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Status", color = ArenaTextSecondary)
                        Text("SUCCESS", color = ArenaGreenSuccess, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Amount", color = ArenaTextSecondary)
                        Text("₹500.00", color = ArenaTextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Reference Gateway", color = ArenaTextSecondary)
                        Text("Razorpay / UPI", color = ArenaTextPrimary)
                    }
                }
            }
        }

        ApexButton(text = "Download Tax Receipt", onClick = onNavigateBack, modifier = Modifier.fillMaxWidth(), leadingIcon = Icons.Default.Download)
    }
}
