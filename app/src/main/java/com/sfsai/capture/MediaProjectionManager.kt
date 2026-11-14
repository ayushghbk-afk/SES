package com.sfsai.capture

import android.content.Context

/**
 * PER SPECIFICATION: The previous screen-capture-based architecture is NOT the intended final architecture.
 * We want genuine game-side integration.
 * 
 * This class is a STUB/INTERFACE definition for the MediaProjectionManager.
 * In MVP 1, we do NOT build a screenshot/OCR-based system as the main architecture.
 * We do NOT simulate direct game integration using fake telemetry.
 * 
 * Implementation is provided as a structural placeholder for future integration
 * if an official API or supported modding interface allows data export.
 */
class MediaProjectionManager(private val context: Context) {
    
    fun requestPermission() {
        // Stub: Permission request logic would go here if screen capture were the main architecture.
        // Per Spec: "Do NOT build a screenshot/OCR-based system as the main architecture."
    }
    
    fun startCapture() {
        // Stub: Start capture logic
    }
    
    fun stopCapture() {
        // Stub: Stop capture logic
    }
    
    fun isAuthorized(): Boolean {
        // Stub: Check permission
        return false
    }
}
