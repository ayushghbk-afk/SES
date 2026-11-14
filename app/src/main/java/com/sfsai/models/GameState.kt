package com.sfsai.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    @SerialName("scene") val scene: String = "unknown", // main_menu, rocket_construction, flight, map, unknown
    @SerialName("confidence") val confidence: Double = 0.0
)

@Serializable
data class Telemetry(
    @SerialName("altitude") val altitude: Double? = null,
    @SerialName("velocity") val velocity: Double? = null,
    @SerialName("fuel") val fuel: Double? = null,
    @SerialName("throttle") val throttle: Double? = null,
    @SerialName("flight_direction") val flightDirection: Double? = null,
    @SerialName("atmospheric_indicators") val atmosphericIndicators: String? = null,
    @SerialName("stage_information") val stageInformation: String? = null,
    @SerialName("confidence") val confidence: Double = 0.0,
    @SerialName("timestamp") val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class Observation(
    @SerialName("type") val type: String = "game_state",
    @SerialName("version") val version: Int = 1,
    @SerialName("source") val source: String = "android_adapter",
    @SerialName("state") val state: GameState = GameState(),
    @SerialName("telemetry") val telemetry: Telemetry = Telemetry(),
    @SerialName("timestamp") val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun empty() = Observation(
            state = GameState("unknown", 0.0),
            telemetry = Telemetry(),
            timestamp = System.currentTimeMillis()
        )
    }
}
