package com.sfsai.networking

import com.sfsai.models.Observation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.json
import io.ktor.client.request.body
import io.ktor.client.websockets.*
import io.ktor.http.ContentType
import io.ktor.http.websocket.*
import java.util.concurrent.atomic.AtomicBoolean

class WebSocketClient(private var serverAddress: String = "ws://localhost:8080/ws") {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
    }
    
    private val job = Job()
    private val scope = CoroutineScope(job)
    
    // FIX: Implement actual connection-state observation using StateFlow
    private val _connectionStateFlow = MutableStateFlow(false)
    val connectionStateFlow: StateFlow<Boolean> = _connectionStateFlow
    
    private val isConnectedAtomic = AtomicBoolean(false)
    var isConnected: Boolean
        get() = isConnectedAtomic.get()
        private set
    
    fun connect() {
        scope.launch {
            try {
                client.webSocket(block = {
                    // Send initial observation (Protocol v1)
                    send(Observation())
                    
                    _connectionStateFlow.value = true
                    isConnectedAtomic.set(true)

                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                // Handle incoming messages from AI engine (Future MVP 2+)
                                val json = frame.readText()
                                // Parse and update state
                            }
                            is Frame.Close -> break
                        }
                    }
                }, url = serverAddress) {
                    // Keep alive / Reconnection logic would go here
                }
            } catch (e: Exception) {
                _connectionStateFlow.value = false
                isConnectedAtomic.set(false)
                // Reconnection backoff logic
            } finally {
                _connectionStateFlow.value = false
                isConnectedAtomic.set(false)
            }
        }
    }
    
    fun disconnect() {
        job.cancel()
        client.close()
        _connectionStateFlow.value = false
        isConnectedAtomic.set(false)
    }
    
    fun sendObservation(observation: Observation) {
        scope.launch {
            if (isConnected) {
                try {
                    // Implementation for sending JSON message (Protocol v1)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}
