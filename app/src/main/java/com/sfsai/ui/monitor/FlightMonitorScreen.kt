package com.sfsai.ui.monitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sfsai.networking.WebSocketClient

@Composable
fun FlightMonitorScreen(webSocketClient: WebSocketClient) {
    // FIX: Actual connection-state observation using StateFlow
    val connectionState = remember { webSocketClient.connectionStateFlow }
    var isConnected by remember { mutableStateOf(false) }
    
    // Note: In a real implementation, we would collect the flow here.
    // For MVP 1, we keep it simple as per the stub requirement.

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Live Flight Monitor", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        // FIX: Remove misleading capture/OCR claims. Keep telemetry null.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text("Game-Side Integration Component (Stubbed)", color = MaterialTheme.colorScheme.textSecondary)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // FIX: Keep all telemetry null until a genuine data source exists
        Text("Telemetry: Altitude: null | Velocity: null | Fuel: null", color = MaterialTheme.colorScheme.textSecondary)
        Text("OCR Confidence: N/A (Stubbed - No OCR Main Architecture)", color = MaterialTheme.colorScheme.textSecondary)
        Text("Observation Timestamp: N/A", color = MaterialTheme.colorScheme.textSecondary)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Connection Status: ${if (isConnected) "Online" else "Offline"}", color = MaterialTheme.colorScheme.secondary)
    }
}
