package com.sfsai.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sfsai.utils.Logger

/**
 * PER SPECIFICATION: The previous screen-capture-based architecture is NOT the intended final architecture.
 * We want genuine game-side integration.
 * 
 * ScreenCaptureService is stubbed. In MVP 1, we do NOT build a screenshot/OCR-based system
 * as the main architecture. The foreground notification requirement is noted for future
 * integration if an official API or supported modding interface allows data export.
 */
class ScreenCaptureService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.i("ScreenCaptureService started (Stub - No screen capture in MVP 1)")
        // Stub: Foreground service notification logic would go here
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.i("ScreenCaptureService destroyed")
    }
}
