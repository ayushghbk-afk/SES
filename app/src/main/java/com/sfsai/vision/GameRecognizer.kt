package com.sfsai.vision

import com.sfsai.models.GameState
import com.sfsai.models.Telemetry

/**
 * PER SPECIFICATION: The previous screen-capture-based architecture is NOT the intended final architecture.
 * We want genuine game-side integration.
 * 
 * GameRecognizer and HudRecognizer are defined as interfaces/stubs.
 * In MVP 1, we do NOT simulate direct game integration using fake telemetry.
 * We return UNKNOWN and null values as specified in the "CRITICAL ARCHITECTURE CHANGE".
 */

interface GameRecognizer {
    fun recognize(frameData: Any?): GameState
}

object GameRecognizerImpl : GameRecognizer {
    override fun recognize(frameData: Any?): GameState {
        // PER SPEC: Return UNKNOWN instead of inventing a game state if recognition fails
        // or if integration is not yet supported (MVP 1 requirement).
        return GameState("unknown", 0.0)
    }
}

interface HudRecognizer {
    fun recognize(frameData: Any?): Telemetry
}

object HudRecognizerImpl : HudRecognizer {
    override fun recognize(frameData: Any?): Telemetry {
        // PER SPEC: Never fabricate telemetry. Store null when value cannot be recognized.
        return Telemetry(
            altitude = null,
            velocity = null,
            fuel = null,
            confidence = 0.0,
            timestamp = System.currentTimeMillis()
        )
    }
}

interface OcrEngine {
    fun process(frameData: Any?): String?
}

object OcrEngineImpl : OcrEngine {
    override fun process(frameData: Any?): String? {
        // Stub: OCR processing logic (Google ML Kit would be used here if integration existed)
        return null
    }
}
