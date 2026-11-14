package com.sfsai.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sfsai.models.GameState
import com.sfsai.models.Observation
import com.sfsai.models.Telemetry
import com.sfsai.networking.WebSocketClient
import com.sfsai.ui.components.StatusCard
import com.sfsai.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

sealed class OperatingMode {
    object Copilot : OperatingMode()
    object Autopilot : OperatingMode()
}

@Composable
fun DashboardScreen(
    mode: OperatingMode,
    onModeChange: (OperatingMode) -> Unit,
    webSocketClient: WebSocketClient
) {
    // ACTUAL CONNECTION-STATE OBSERVATION USING STATEFLOW
    val connectionState = remember { webSocketClient.connectionStateFlow }
    var isConnected by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        connectionState.collectLatest { state ->
            isConnected = state
        }
    }

    // ACTUAL OBSERVATION STATE (Null telemetry until data source exists)
    var observation by remember { mutableStateOf(Observation.empty()) }
    LaunchedEffect(Unit) {
        // In MVP 1, we observe the client but do NOT fake telemetry.
        // We only report actual WebSocket status and null telemetry.
        webSocketClient.connect()
        // Future integration: connectionStateFlow.collect { ... update observation ... }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("SFS AI Companion", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Operating Mode: ${if (mode == OperatingMode.Copilot) "Copilot" else "Autopilot (Coming Soon)"}",
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusCard("App Status", "Active", MaterialTheme.colorScheme.primary)
                // FIX: Use actual connection state observation
                StatusCard("SFS Connection", if (isConnected) "Connected" else "Disconnected", MaterialTheme.colorScheme.secondary)
                // FIX: Remove misleading capture permission/status (per Spec: stubbed/no screen capture main)
                StatusCard("Capture", "Stopped (Stubbed)", MaterialTheme.colorScheme.warning)
                StatusCard("AI Engine", "Disconnected", MaterialTheme.colorScheme.error)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            // FIX: Keep telemetry null until genuine data source exists
            Text("Current Scene: ${observation.state.scene}", color = MaterialTheme.colorScheme.primary)
            Text("Last Observation: ${observation.timestamp}", color = MaterialTheme.colorScheme.textSecondary)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { onModeChange(if (mode == OperatingMode.Copilot) OperatingMode.Autopilot else OperatingMode.Copilot) },
                enabled = mode == OperatingMode.Copilot, // Autopilot remains disabled
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (mode == OperatingMode.Copilot) "Switch to Autopilot (Coming Soon)" else "Switch to Copilot")
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text("System Diagnostics", style = MaterialTheme.typography.titleMedium)
            Text("WebSocket: ${if (isConnected) "Online" else "Offline"}", color = MaterialTheme.colorScheme.textSecondary)
            Text("Version: 1.0.0 (MVP 1)", color = MaterialTheme.colorScheme.textSecondary)
        }
    }
}
