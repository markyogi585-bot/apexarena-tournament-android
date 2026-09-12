package com.apex.arena.core.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickJoinSheet(
    tournamentTitle: String,
    entryFee: Double,
    availableBalance: Double,
    onConfirmJoin: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Confirm Tournament Entry", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            Text(tournamentTitle, style = MaterialTheme.typography.bodyMedium, color = ArenaTextSecondary)

            HorizontalDivider(color = ArenaBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Entry Fee", color = ArenaTextSecondary)
                Text(if (entryFee == 0.0) "FREE" else "₹${entryFee.toInt()}", color = ArenaAccentRose, fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Wallet Balance", color = ArenaTextSecondary)
                Text("₹${availableBalance.toInt()}", color = ArenaTextPrimary, fontWeight = FontWeight.Bold)
            }

            if (availableBalance < entryFee) {
                Text("Insufficient balance. Please add cash to proceed.", color = ArenaErrorRed, fontSize = 12.sp)
            }

            ApexButton(
                text = if (availableBalance >= entryFee) "Confirm & Reserve Slot" else "Add Cash First",
                onClick = onConfirmJoin,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.SportsEsports
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCashModalSheet(
    onAddCash: (Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("500") }
    val quickAmounts = listOf("100", "250", "500", "1000", "2000")
    var selectedMethod by remember { mutableStateOf("UPI / QR") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Deposit Cash to Wallet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            
            ApexTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Deposit Amount (₹)",
                leadingIcon = Icons.Default.CurrencyRupee,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                quickAmounts.forEach { amt ->
                    FilterChip(
                        selected = amount == amt,
                        onClick = { amount = amt },
                        label = { Text("+₹$amt", color = if (amount == amt) ArenaTextPrimary else ArenaTextSecondary) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ArenaPurplePrimary,
                            containerColor = ArenaDarkSurface
                        )
                    )
                }
            }

            Text("Payment Method", style = MaterialTheme.typography.labelLarge, color = ArenaTextSecondary)
            listOf("UPI / QR", "Debit / Credit Card", "Net Banking").forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedMethod == method) ArenaPurplePrimary.copy(alpha = 0.2f) else ArenaDarkSurface)
                        .clickable { selectedMethod = method }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedMethod == method,
                        onClick = { selectedMethod = method },
                        colors = RadioButtonDefaults.colors(selectedColor = ArenaPurplePrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(method, color = ArenaTextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            ApexButton(
                text = "Proceed to Pay ₹${amount.ifBlank { "0" }}",
                onClick = {
                    val amtVal = amount.toDoubleOrNull() ?: 0.0
                    if (amtVal > 0) onAddCash(amtVal, selectedMethod)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Lock
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawModalSheet(
    maxBalance: Double,
    onWithdraw: (Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("1000") }
    var upiId by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Instant Payout (IMPS / UPI)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            Text("Available for withdrawal: ₹${maxBalance.toInt()}", color = ArenaGreenSuccess, fontSize = 13.sp)

            ApexTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Withdraw Amount (₹)",
                leadingIcon = Icons.Default.CurrencyRupee,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ApexTextField(
                value = upiId,
                onValueChange = { upiId = it },
                label = "UPI VPA / Bank Account (e.g. user@okhdfcbank)",
                leadingIcon = Icons.Default.AccountBalance
            )

            ApexButton(
                text = "Request Instant Transfer",
                onClick = {
                    val amtVal = amount.toDoubleOrNull() ?: 0.0
                    if (amtVal > 0 && upiId.isNotBlank()) onWithdraw(amtVal, upiId)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.AccountBalanceWallet
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchRoomCredentialsSheet(
    roomId: String,
    roomPass: String,
    slotNumber: Int,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    var copiedText by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.VpnKey, contentDescription = null, tint = ArenaAccentRose)
                Text("Match Room Credentials", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }
            Text("Copy these credentials into the game lobby. Do not share with unauthorized players.", color = ArenaTextSecondary, fontSize = 12.sp)

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("ROOM ID", fontSize = 11.sp, color = ArenaTextMuted)
                            Text(roomId, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                        }
                        IconButton(onClick = {
                            clipboard.setText(AnnotatedString(roomId))
                            copiedText = "Room ID Copied!"
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Room ID", tint = ArenaPurpleLight)
                        }
                    }
                    HorizontalDivider(color = ArenaBorder)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("PASSWORD", fontSize = 11.sp, color = ArenaTextMuted)
                            Text(roomPass, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                        }
                        IconButton(onClick = {
                            clipboard.setText(AnnotatedString(roomPass))
                            copiedText = "Password Copied!"
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Password", tint = ArenaPurpleLight)
                        }
                    }
                    HorizontalDivider(color = ArenaBorder)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ASSIGNED SLOT", fontSize = 11.sp, color = ArenaTextMuted)
                        Text("Slot #$slotNumber", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ArenaGreenSuccess)
                    }
                }
            }

            copiedText?.let {
                Text(it, color = ArenaGreenSuccess, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            ApexButton(text = "Dismiss", onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTournamentsSheet(
    selectedGame: String,
    selectedFormat: String,
    onApply: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var game by remember { mutableStateOf(selectedGame) }
    var format by remember { mutableStateOf(selectedFormat) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Filter Tournaments", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)

            Text("Select Game", color = ArenaTextSecondary, fontSize = 13.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "BGMI", "Free Fire", "Valorant").forEach { g ->
                    FilterChip(
                        selected = game == g,
                        onClick = { game = g },
                        label = { Text(g) }
                    )
                }
            }

            Text("Tournament Format", color = ArenaTextSecondary, fontSize = 13.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "SOLO", "DUO", "SQUAD").forEach { f ->
                    FilterChip(
                        selected = format == f,
                        onClick = { format = f },
                        label = { Text(f) }
                    )
                }
            }

            ApexButton(
                text = "Apply Filters",
                onClick = { onApply(game, format) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.FilterList
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDisputeReportSheet(
    matchId: String,
    onSubmitDispute: (reason: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("Hacking / Illegal Software") }
    var description by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Report Match Dispute", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            Text("Dispute for Match #$matchId", color = ArenaTextSecondary, fontSize = 12.sp)

            listOf("Hacking / Illegal Software", "Wrong Room Results", "Teaming / Collusion", "No-show / AFK").forEach { r ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (reason == r) ArenaPurplePrimary.copy(alpha = 0.2f) else ArenaDarkSurface)
                        .clickable { reason = r }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = reason == r, onClick = { reason = r })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(r, color = ArenaTextPrimary)
                }
            }

            ApexTextField(
                value = description,
                onValueChange = { description = it },
                label = "Detailed Explanation & Timestamps",
                singleLine = false
            )

            ApexButton(
                text = "Submit Formal Dispute",
                onClick = { onSubmitDispute(reason, description) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Gavel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycUploadSheet(
    onSubmitKyc: (fullName: String, docType: String, docNumber: String) -> Unit,
    onDismiss: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("PAN") }
    var docNumber by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("KYC Verification", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            Text("Mandatory for tax compliance & bank withdrawals.", color = ArenaTextSecondary, fontSize = 12.sp)

            ApexTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Legal Name")
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("PAN", "AADHAAR", "PASSPORT").forEach { dt ->
                    FilterChip(selected = docType == dt, onClick = { docType = dt }, label = { Text(dt) })
                }
            }

            ApexTextField(value = docNumber, onValueChange = { docNumber = it }, label = "$docType Number")

            ApexButton(
                text = "Submit for Instant Verification",
                onClick = { onSubmitKyc(fullName, docType, docNumber) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.VerifiedUser
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamSheet(
    onCreateTeam: (name: String, tag: String) -> Unit,
    onDismiss: () -> Unit
) {
    var teamName by remember { mutableStateOf("") }
    var teamTag by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Create Esports Squad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)

            ApexTextField(value = teamName, onValueChange = { teamName = it }, label = "Squad / Team Name (e.g. GodLike)")
            ApexTextField(value = teamTag, onValueChange = { teamTag = it }, label = "Team Tag [e.g. GOD]")

            ApexButton(
                text = "Create Squad",
                onClick = { onCreateTeam(teamName, teamTag) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.GroupAdd
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamInviteMemberSheet(
    onSendInvite: (gamerTag: String) -> Unit,
    onDismiss: () -> Unit
) {
    var gamerTag by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Invite Player to Squad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            ApexTextField(value = gamerTag, onValueChange = { gamerTag = it }, label = "Enter GamerTag / Player ID")

            ApexButton(
                text = "Send Squad Invite",
                onClick = { onSendInvite(gamerTag) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Send
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ArenaDarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = ArenaBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArenaSpacing.lg)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(ArenaSpacing.md)
        ) {
            Text("Apex Arena Fair Play & Terms", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            Text(
                "1. Zero Tolerance Anti-Cheat: Use of game modifications, emulators in mobile-only brackets, or teaming will result in a permanent ban.\n" +
                "2. Auditable Rewards: All tournament winnings and points are subject to match review and identity verification before withdrawal.\n" +
                "3. Room Credentials Policy: Never leak room IDs and passwords to unregistered players.",
                color = ArenaTextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
            ApexButton(text = "I Understand & Agree", onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}
