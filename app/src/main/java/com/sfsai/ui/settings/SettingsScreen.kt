package com.sfsai.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sfsai.storage.SettingsRepository

@Composable
fun SettingsScreen(
    settingsRepo: SettingsRepository,
    onModeChange: (OperatingMode) -> Unit
) {
    var address by remember { mutableStateOf(settingsRepo.websocketAddress) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = address,
            onValueChange = { 
                address = it
                settingsRepo.websocketAddress = it 
            },
            label = { Text("WebSocket Server Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // FIX: Remove misleading capture permission status. Keep it clean.
        Text("Capture Permission Status: Not Requested (Stubbed Architecture)", color = MaterialTheme.colorScheme.textSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Request Permission Logic - Stubbed */ }) {
            Text("Request Capture Permission (Stub)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Application Version: 1.0.0", color = MaterialTheme.colorScheme.textSecondary)
        Text("Privacy: Data stays local. No external uploads.", color = MaterialTheme.colorScheme.textSecondary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onModeChange(OperatingMode.Copilot) }) { Text("Copilot Mode") }
            // FIX: Ensure Autopilot remains disabled
            Button(onClick = { onModeChange(OperatingMode.Autopilot) }, enabled = false) { Text("Autopilot Mode (Coming Soon)") }
        }
    }
}

sealed class OperatingMode {
    object Copilot : OperatingMode()
    object Autopilot : OperatingMode()
}
