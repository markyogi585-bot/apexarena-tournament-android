package com.apex.arena.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel
) {
    var username by remember { mutableStateOf("") }
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
        Text(
            text = "CREATE ACCOUNT",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = TextCrisp
        )
        Text(
            text = "Join thousands of competitive gamers",
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = Dimensions.spaceXL)
        )

        ApexTextField(
            value = username,
            onValueChange = { username = it },
            label = "Gamer Tag / Username",
            placeholder = "ShadowStriker",
            leadingIcon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

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
            text = "Create Gamer Profile",
            onClick = {
                viewModel.register(email, password, username)
                onRegisterSuccess()
            }
        )

        Spacer(modifier = Modifier.height(Dimensions.spaceM))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account?", color = TextMuted, fontSize = 14.sp)
            TextButton(onClick = onNavigateToLogin) {
                Text(text = "Log In", color = SoftLilac, fontWeight = FontWeight.Bold)
            }
        }
    }
}
