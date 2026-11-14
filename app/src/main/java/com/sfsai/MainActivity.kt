package com.sfsai

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sfsai.models.GameState
import com.sfsai.models.Observation
import com.sfsai.models.Telemetry
import com.sfsai.networking.WebSocketClient
import com.sfsai.storage.SettingsRepository
import com.sfsai.ui.components.StatusCard
import com.sfsai.ui.dashboard.DashboardScreen
import com.sfsai.ui.monitor.FlightMonitorScreen
import com.sfsai.ui.settings.SettingsScreen
import com.sfsai.utils.Logger

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Settings and WebSocket
        val settingsRepo = SettingsRepository(this)
        val webSocketClient = WebSocketClient(settingsRepo.websocketAddress)
        
        setContent {
            SFSAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(settingsRepo, webSocketClient)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(settingsRepo: SettingsRepository, webSocketClient: WebSocketClient) {
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }
    var operatingMode by remember { mutableStateOf(OperatingMode.Copilot) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SFS AI — Companion", color = MaterialTheme.colorScheme.primary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Screen.Dashboard,
                    onClick = { currentScreen = Screen.Dashboard },
                    label = { Text("Dashboard") },
                    icon = { /* Icon placeholder */ }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Monitor,
                    onClick = { currentScreen = Screen.Monitor },
                    label = { Text("Monitor") },
                    icon = { /* Icon placeholder */ }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Settings,
                    onClick = { currentScreen = Screen.Settings },
                    label = { Text("Settings") },
                    icon = { /* Icon placeholder */ }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (currentScreen) {
                Screen.Dashboard -> DashboardScreen(operatingMode, ::setOperatingMode, webSocketClient)
                Screen.Monitor -> FlightMonitorScreen(webSocketClient)
                Screen.Settings -> SettingsScreen(settingsRepo, ::setOperatingMode)
            }
        }
    }
}

@Composable
fun SFSAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF7C5CFF),
            secondary = Color(0xFF26D6A6),
            background = Color(0xFF0B1020),
            surface = Color(0xFF151D32),
            error = Color(0xFFFF5C70),
            warning = Color(0xFFFFB547)
        ),
        content = content
    )
}

sealed class Screen {
    object Dashboard : Screen()
    object Monitor : Screen()
    object Settings : Screen()
}

sealed class OperatingMode {
    object Copilot : OperatingMode()
    object Autopilot : OperatingMode()
}

fun String.toColor(): Color = Color(android.graphics.Color.parseColor(this))

@Composable
fun DashboardScreen(
    mode: OperatingMode,
    onModeChange: (OperatingMode) -> Unit,
    webSocketClient: WebSocketClient
) {
    var observation by remember { mutableStateOf(Observation.empty()) }
    
    LaunchedEffect(Unit) {
        // In MVP 1, we simulate the structure but do NOT fake telemetry.
        // We listen to WebSocket client status.
        webSocketClient.connect()
        // Periodic update simulation (would be real data in future)
        // For MVP 1 compliance: "Never fake telemetry or fake connection status"
        // We only report actual WebSocket status.
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("SFS AI Companion", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Operating Mode: ${if (mode == OperatingMode.Copilot) "Copilot" else "Autopilot (Coming Soon)"}", 
                 color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusCard("App Status", "Active", MaterialTheme.colorScheme.primary)
                StatusCard("SFS Connection", webSocketClient.isConnected.toString(), MaterialTheme.colorScheme.secondary)
                StatusCard("Capture", "Stopped (Authorized)", MaterialTheme.colorScheme.warning)
                StatusCard("AI Engine", "Disconnected", MaterialTheme.colorScheme.error)
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Current Scene: ${observation.state.scene}", color = MaterialTheme.colorScheme.primary)
            Text("Last Observation: ${observation.timestamp}", color = MaterialTheme.colorScheme.textSecondary)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            Button(
                onClick = { onModeChange(if (mode == OperatingMode.Copilot) OperatingMode.Autopilot else OperatingMode.Copilot) },
                enabled = mode == OperatingMode.Copilot, // Autopilot is coming soon
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (mode == OperatingMode.Copilot) "Switch to Autopilot (Coming Soon)" else "Switch to Copilot")
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text("System Diagnostics", style = MaterialTheme.typography.titleMedium)
            Text("WebSocket: ${if (webSocketClient.isConnected) "Online" else "Offline"}", color = MaterialTheme.colorScheme.textSecondary)
            Text("Version: 1.0.0 (MVP 1)", color = MaterialTheme.colorScheme.textSecondary)
        }
    }
}

@Composable
fun StatusCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.textSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FlightMonitorScreen(webSocketClient: WebSocketClient) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Live Flight Monitor", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text("Screenshot Preview (Authorized Capture Only)", color = MaterialTheme.colorScheme.textSecondary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Telemetry: Altitude: null | Velocity: null | Fuel: null", color = MaterialTheme.colorScheme.textSecondary)
        Text("OCR Confidence: N/A", color = MaterialTheme.colorScheme.textSecondary)
        Text("Observation Timestamp: N/A", color = MaterialTheme.colorScheme.textSecondary)
    }
}

@Composable
fun SettingsScreen(settingsRepo: SettingsRepository, onModeChange: (OperatingMode) -> Unit) {
    var address by remember { mutableStateOf(settingsRepo.websocketAddress) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = address,
            onValueChange = { address = it; settingsRepo.websocketAddress = it },
            label = { Text("WebSocket Server Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Capture Permission Status: Not Requested", color = MaterialTheme.colorScheme.textSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Request Permission Logic */ }) {
            Text("Request Capture Permission")
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Application Version: 1.0.0", color = MaterialTheme.colorScheme.textSecondary)
        Text("Privacy: Data stays local. No external uploads.", color = MaterialTheme.colorScheme.textSecondary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onModeChange(OperatingMode.Copilot) }) { Text("Copilot Mode") }
            Button(onClick = { onModeChange(OperatingMode.Autopilot) }, enabled = false) { Text("Autopilot Mode (Coming Soon)") }
        }
    }
}
