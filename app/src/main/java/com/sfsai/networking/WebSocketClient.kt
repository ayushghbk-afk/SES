package com.sfsai.networking

import com.sfsai.models.Observation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    private val isConnectedAtomic = AtomicBoolean(false)
    
    var isConnected: Boolean
        get() = isConnectedAtomic.get()
        private set
    
    fun connect() {
        scope.launch {
            try {
                client.webSocket(block = {
                    send(Observation()) // Initial handshake/observation
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                // Handle incoming messages from AI engine
                                val json = frame.readText()
                                // Parse and update state
                            }
                            is Frame.Close -> break
                        }
                    }
                }, url = serverAddress) {
                    isConnectedAtomic.set(true)
                    // Keep alive / Reconnection logic would go here
                }
            } catch (e: Exception) {
                isConnectedAtomic.set(false)
                // Reconnection backoff logic
            } finally {
                isConnectedAtomic.set(false)
            }
        }
    }
    
    fun disconnect() {
        job.cancel()
        client.close()
        isConnectedAtomic.set(false)
    }
    
    fun sendObservation(observation: Observation) {
        scope.launch {
            if (isConnected) {
                try {
                    // Implementation for sending JSON message
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}
