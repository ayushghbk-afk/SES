# Build Instructions for SFS AI — Android Companion Application (Corrected)

## Prerequisites
1. **Android Studio** (latest version, compatible with Android 16 / API 36)
2. **Kotlin** (1.9.x or later)
3. **Gradle** (8.x or later)
4. **JDK** (17)
5. **Android SDK** (API 23 to API 36)

## Steps to Build
1. **Clone the Repository**
   ```bash
   git clone https://github.com/ayushghbk-afk/SES.git
   cd SES
   git checkout arena/9bc32600-ses
   ```

2. **Open in Android Studio**
   - Open Android Studio.
   - Select "Open an existing Android Studio project".
   - Select the `SES` directory.

3. **Sync Project with Gradle Files**
   - Wait for Android Studio to sync dependencies (Kotlin Coroutines 1.8.0, Ktor 2.3.12, Material 3 1.3.0).
   - **Note**: ML Kit dependency was removed from main architecture per technical correction.

4. **Build the Debug APK**
   - Go to `Build` > `Build Bundle(s) / APK(s)` > `Build APK(s)`.
   - The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

5. **Run on Target Device (Samsung Galaxy F15, Android 16)**
   - Enable Developer Options and USB Debugging on the device.
   - Connect the device via USB.
   - Select the device in Android Studio and click "Run".

## Installation Instructions
1. **Direct Install via Android Studio**
   - Use the "Run" button in Android Studio.

2. **Manual Install**
   - Transfer `app-debug.apk` to the Samsung Galaxy F15.
   - Install the APK.
   - Grant necessary permissions (Internet, Notifications).

## Testing Instructions (Technical Correction Focus)
1. **Application Launch**
   - Verify the Dashboard opens without crashing.
   - Check that Operating Modes (Copilot/Autopilot) are visible.
   - Verify Autopilot is marked "Coming Soon" and **disabled**.

2. **WebSocket Connection (StateFlow Observation)**
   - Open Settings and configure the WebSocket server address (default: `ws://localhost:8080/ws`).
   - Verify the connection status indicator in the Dashboard updates via **StateFlow**.
   - Test connection failure handling.

3. **Game Recognition & HUD (Genuine Integration Stub)**
   - Verify that the application reports "UNKNOWN" scene and "null" telemetry (per Spec: "Never fake telemetry").
   - Verify confidence scores are 0.0.
   - Verify **GameAdapter interface** is present for future direct integration.

4. **Capture Permission & OCR (Removed from Main Architecture)**
   - Verify the permission request button is present but marked as **Stubbed** (per Critical Architecture Change).
   - Verify no OCR processing is attempted on the main thread.

5. **Unit Tests**
   - Run `./gradlew test` to verify JSON serialization and model logic.

## Known Limitations (MVP 1 - Corrected)
1. **Game-Side Integration**: The official SFS Android app (`com.StefMorojna.SpaceflightSimulator`) does not currently provide an external API or executable modding interface for real-time telemetry. The application architecture is prepared for future integration (MVP 2+).
2. **Screen Capture**: **NOT** the main architecture. MediaProjectionManager and ScreenCaptureService are stubbed.
3. **OCR**: **Removed** from main architecture. Google ML Kit dependency removed from build.gradle.
4. **Autopilot**: Marked as "Coming Soon". No autonomous control is implemented.
5. **Telemetry**: All telemetry values are **null**. No fake data is generated.

## Implemented Features (MVP 1 - Corrected)
- [x] Complete Android Studio project structure (Fixed).
- [x] Kotlin source files (MainActivity, Models, Networking, Storage, Utils, Stubs, Screens).
- [x] Gradle build configuration (Kotlin DSL, Android 16 target, Corrected Dependencies).
- [x] AndroidManifest.xml (Permissions, Activity, Service stub).
- [x] Resources (Dark space-themed colors, strings, themes).
- [x] Main Dashboard (Status cards, Operating modes, Diagnostics, **StateFlow Observation**).
- [x] Live Flight Monitor (Placeholder for telemetry and game-side integration stub).
- [x] Settings (WebSocket address, Debug mode, Privacy info, **Autopilot Disabled**).
- [x] Communication Foundation (WebSocket client with **StateFlow** and Protocol v1).
- [x] Data Models (GameState, Telemetry, Observation - JSON serializable).
- [x] Technical Correction Report (`TECHNICAL_CORRECTION_REPORT.md`).

## Incomplete Features (Future Milestones)
- [ ] MVP 2: Python AI engine connection.
- [ ] MVP 3: MCP server and tools.
- [ ] MVP 4: Rocket and orbital analysis.
- [ ] MVP 5: Copilot recommendations.
- [ ] MVP 6: Supervised autopilot.
- [ ] MVP 7: Genuine game-side integration (awaiting official API or supported modding interface).

---
**Project Lead:** Android Developer (Arena.ai Agent)
**Date:** 2026-11-14
**Branch:** `arena/9bc32600-ses`
