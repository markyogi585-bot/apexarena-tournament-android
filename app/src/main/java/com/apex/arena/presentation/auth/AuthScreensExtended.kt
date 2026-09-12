package com.apex.arena.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.theme.*

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1200)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ArenaDarkBase, ArenaDarkSurface)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ArenaPurplePrimary, ArenaAccentRose)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.SportsEsports,
                    contentDescription = "Apex Arena Logo",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                "APEX ARENA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = ArenaTextPrimary,
                letterSpacing = 2.sp
            )
            Text(
                "COMPETITIVE ESPORTS & REWARDS",
                style = MaterialTheme.typography.labelMedium,
                color = ArenaPurpleLight,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ArenaPurplePrimary, ArenaAccentRose)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }
            Text(
                "Compete. Dominate. Earn.",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ArenaTextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                "Join official automated tournaments in BGMI, Free Fire, and Valorant with instant reward passbooks.",
                style = MaterialTheme.typography.bodyMedium,
                color = ArenaTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ApexButton(
                text = "Create Free Account",
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.PersonAdd
            )
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ArenaPurplePrimary)
            ) {
                Text("Sign In to Existing Account", color = ArenaTextPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOtp: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
        }

        Text("Reset Password", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        Text("Enter your verified email to receive a 6-digit authentication OTP.", style = MaterialTheme.typography.bodyMedium, color = ArenaTextSecondary)

        ApexTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            leadingIcon = Icons.Default.Email
        )

        ApexButton(
            text = "Send Reset OTP",
            onClick = {
                if (email.isNotBlank()) {
                    isLoading = true
                    onNavigateToOtp(email)
                }
            },
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Send
        )
    }
}

@Composable
fun VerificationOtpScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onVerified: () -> Unit
) {
    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
        }

        Text("Verify Security OTP", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        Text("We sent a 6-digit code to $email", style = MaterialTheme.typography.bodyMedium, color = ArenaTextSecondary)

        ApexTextField(
            value = otp,
            onValueChange = { if (it.length <= 6) otp = it },
            label = "6-Digit OTP Code",
            leadingIcon = Icons.Default.VpnKey
        )

        ApexButton(
            text = "Verify & Proceed",
            onClick = {
                if (otp.length == 6) onVerified()
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.CheckCircle
        )
    }
}

@Composable
fun ResetPasswordScreen(
    onNavigateToLogin: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg)
            .navigationBarsPadding()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Set New Password", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        Text("Your new password must be at least 8 characters with numbers and symbols.", style = MaterialTheme.typography.bodyMedium, color = ArenaTextSecondary)

        ApexTextField(
            value = password,
            onValueChange = { password = it },
            label = "New Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        ApexTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm New Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        ApexButton(
            text = "Update Password",
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Save
        )
    }
}

@Composable
fun SessionExpiredScreen(
    onReAuthenticate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.HourglassDisabled, contentDescription = null, tint = ArenaAccentRose, modifier = Modifier.size(56.dp))
                Text("Session Expired", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                Text("Your security token has expired. Please sign in again to continue your tournament activity.", color = ArenaTextSecondary, textAlign = TextAlign.Center)
                ApexButton(
                    text = "Sign In Again",
                    onClick = onReAuthenticate,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = Icons.Default.Login
                )
            }
        }
    }
}
