package com.apex.arena.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apex.arena.core.components.ApexPrimaryButton
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.ObsidianDark

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var displayName by remember { mutableStateOf(uiState.profile?.displayName ?: "") }
    var gameId by remember { mutableStateOf(uiState.profile?.gameId ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Edit Profile",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.spaceM)
        ) {
            ApexTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = "Display Name",
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            ApexTextField(
                value = gameId,
                onValueChange = { gameId = it },
                label = "In-Game UID / Character ID",
                leadingIcon = Icons.Default.Badge
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            ApexPrimaryButton(
                text = "SAVE CHANGES",
                onClick = {
                    viewModel.updateProfile(displayName, gameId)
                    onNavigateBack()
                }
            )
        }
    }
}
