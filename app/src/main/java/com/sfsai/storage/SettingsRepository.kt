package com.sfsai.storage

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("sfs_ai_prefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    var websocketAddress: String
        get() = prefs.getString("websocket_address", "ws://localhost:8080/ws") ?: "ws://localhost:8080/ws"
        set(value) {
            editor.putString("websocket_address", value).apply()
        }

    var operatingMode: String
        get() = prefs.getString("operating_mode", "copilot") ?: "copilot"
        set(value) {
            editor.putString("operating_mode", value).apply()
        }

    var debugMode: Boolean
        get() = prefs.getBoolean("debug_mode", false)
        set(value) {
            editor.putBoolean("debug_mode", value).apply()
        }
        
    var capturePermissionGranted: Boolean
        get() = prefs.getBoolean("capture_permission", false)
        set(value) {
            editor.putBoolean("capture_permission", value).apply()
        }
}
