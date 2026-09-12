package com.apex.arena.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexPrimaryButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.theme.CrimsonError
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(Dimensions.spaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsEsports,
            contentDescription = null,
            tint = NeonViolet,
            modifier = Modifier.padding(bottom = Dimensions.spaceM)
        )
        Text(
            text = "APEX ARENA",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            color = TextCrisp
        )
        Text(
            text = "Enter the Competitive Battleground",
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = Dimensions.spaceXL)
        )

        ApexTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            placeholder = "player@apexarena.gg",
            leadingIcon = Icons.Default.Email
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        ApexTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceL))

        ApexPrimaryButton(
            text = "Enter Arena",
            onClick = {
                viewModel.login(email, password)
                onLoginSuccess()
            }
        )

        Spacer(modifier = Modifier.height(Dimensions.spaceM))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?", color = TextMuted, fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister) {
                Text(text = "Sign Up", color = SoftLilac, fontWeight = FontWeight.Bold)
            }
        }
    }
}
