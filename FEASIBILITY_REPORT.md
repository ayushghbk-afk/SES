# Technical Feasibility Report: SFS AI — Android Companion Application
**Project:** SFS AI (Spaceflight Simulator AI)  
**Date:** 2026-11-14  
**Target Device:** Samsung Galaxy F15 (Android 16, ARM64)  
**Branch:** `arena/f7275d5e-ses`  

---

## 1. Original Android SFS Version and Package
- **Package Name:** `com.StefMorojna.SpaceflightSimulator`
- **Developer:** Stefo Mai Morojna (Team Curiosity ✦)
- **Latest Version (as of Nov 2026):** v1.6.00.22 (Updated Apr 30, 2026) [1][2][4]
- **Minimum Android Version:** Android 6.0+ (API 23) [2][3]
- **Target Android Version:** Android 16 (API 36) [Project Spec]
- **Architecture:** arm64-v8a, armeabi-v7a [3]

## 2. Documented Modding Interfaces
- **Mobile Modding Support:**
  - As of late 2025/2026, mobile modding is officially supported but restricted to **Assets** (Textures and Parts) [3][5].
  - **Mod Folder Location:** `Android/media/com.StefMorojna.SpaceflightSimulator/Mods/` [1][3]
  - **Sub-folders:**
    - `Custom Assets/Texture Packs/` (Texture packs)
    - `Custom Assets/Parts/` (`.pack` files for custom parts) [1][3][5]
- **Limitations:**
  - **Executable Extensions:** `.dll` files (common in PC Steam version) **do not work** on Android. Android does not allow custom native code execution via the mod loader unless the game itself explicitly loads it (currently unsupported on Android) [1][5].
  - **No Scripting API:** There is **no documented public Java/Kotlin/C# API** or scripting interface exposed by the official SFS Android application for external applications to read internal game state or inject commands directly [2][5].
  - **File Access:** External apps can read/write to `Android/media/com.StefMorojna.SpaceflightSimulator/` if granted storage permissions, but this is limited to asset files, not active memory or internal game state [1][2].

## 3. Executable Game-Side Extensions Support
- **Status:** **NOT SUPPORTED** on Android.
- Unlike the PC version (which supports `.dll` mods), Android's security model and the SFS Android implementation restrict modding to asset packs (`.pack`, textures) [1][5].
- Attempting to run executable code inside the SFS process or via `.pack` files is **not supported** and would violate Android security restrictions and the game's architecture [5].

## 4. Game State and Controls Access
- **Official Access:** **None documented.**
  - The SFS Android application does **not** expose an Intent, Broadcast, Content Provider, or Service API for external companion apps to query telemetry (altitude, velocity, fuel) or send control commands [6].
  - Permissions listed in APK analysis (`android.permission.INTERNET`, `android.permission.POST_NOTIFICATIONS`, etc.) are for internal game functions (ads, billing, notifications), not external companion communication [3].
- **Unofficial / Indirect Access:**
  - **File-based:** The game reads/writes blueprint (`.bp`), solar system, and part data to `Android/media/com.StefMorojna.SpaceflightSimulator/files/`. An external app could theoretically parse these files **offline** (when the game is not running or between sessions) to read rocket structure or component info, but **real-time telemetry** (altitude, velocity) is **not stored in these files** during active flight [2].
  - **Screen Capture (Deprecated by Spec):** While technically possible via Android MediaProjection API, the specification explicitly forbids this as the *main* architecture [Project Spec].
  - **Root/Injection:** Requires root access, which is forbidden by the Development Rules [Project Spec].

## 5. Companion Communication Feasibility
- **Direct IPC (Intents/Broadcasts):** **Not supported.** No documented intent filters or broadcast actions exist in the SFS Android APK for external communication [6].
- **Local Network / WebSocket / Socket:** **Not supported** by the official SFS Android app. The game does not open a local server port for telemetry.
- **Recommended Implementation (Game-Side Adapter):**
  - Since the official game does **not** provide an integration component, a "Game-Side Adapter" would require **modding the game assets** (which is restricted to `.pack` files and textures) or **creating a separate companion component** that the user manually installs.
  - **Feasible Approach for MVP 1:**
    1. **Android AI Application:** Built as requested (Kotlin, Jetpack Compose, Material 3).
    2. **Communication Protocol:** Define a JSON/WebSocket protocol for future integration.
    3. **Game-Side Adapter (Future/MVP 2+):** Since executable code is not supported in `.pack` files on Android, the "Game-Side Adapter" must be defined as a **specification** for a future integration method (e.g., if the game developer ever exposes an API, or if a future modding interface allows data export via file polling).
    4. **Current Limitation:** For MVP 1, the application **cannot** read live telemetry from the official SFS Android app without root or screen capture (both restricted/forbidden as main architecture).
    5. **Workaround for MVP 1:** Implement the **Android AI Application** with the **Communication Foundation** (WebSocket client) and **Models**, but mark the **Game-Side Adapter** as **"Integration Interface Defined - Awaiting Official API or Future Modding Support"**. The application will simulate the *structure* of communication while acknowledging that the actual game-side component (which would read the game state) is **not currently implementable** on the official Android version without violating rules (root/reverse engineering) or using the forbidden screen-capture method.
    6. **Alternative (File Polling):** The companion app could poll `Android/media/com.StefMorojna.SpaceflightSimulator/files/` for blueprint changes, but this does **not** provide real-time flight telemetry.

## 6. Comparison of Approaches
| Approach | Feasibility | Legality/Safety | Spec Compliance |
|----------|-------------|-----------------|-----------------|
| **Official API / Intent** | **Impossible** (None exists) | Safe | Compliant |
| **Screen Capture (MediaProjection)** | **Possible** | Safe (User Auth) | **FORBIDDEN** (Critical Architecture Change) |
| **Root / Injection** | **Possible** | **Unsafe / Illegal** | **FORBIDDEN** (Development Rules) |
| **Asset Modding (.pack)** | **Limited** (Parts/Textures only) | Safe | Compliant (but no telemetry) |
| **File Polling (Blueprints)** | **Partial** (Structure only) | Safe | Compliant (but no telemetry) |
| **Proposed MVP 1 Architecture** | **Compliant** | Safe | **Compliant** (Build App + Foundation + Report Limitations) |

## 7. Recommended Implementation for MVP 1
1. **Android AI Application:**
   - Build the complete Kotlin/Jetpack Compose application (`com.sfsai`).
   - Implement **Dashboard**, **Settings**, **Operating Modes** (Copilot/Autopilot), and **Communication Foundation** (WebSocket Client).
   - Implement **Models** (GameState, Telemetry, Observation) with `null` values for telemetry where integration is not possible.
2. **Communication Protocol:**
   - Define JSON schema for `game_state` (version 1).
   - Implement WebSocket client with reconnection logic.
3. **Game-Side Adapter (Specification Only):**
   - Document that the **Game-Side Adapter** must eventually bridge the gap.
   - **Current Reality:** Since the official SFS Android app (`com.StefMorojna.SpaceflightSimulator`) does **not** support executable extensions or external telemetry APIs, the **Game-Side Adapter** for MVP 1 will be a **stub/interface definition** that explains the required integration points (e.g., "If SFS provides an Intent/Broadcast in v1.7+, connect here").
   - **Constraint:** Do **not** build a screenshot-based system. Do **not** simulate fake telemetry.
4. **Deliverables:**
   - Complete Android Studio project.
   - README.md with feasibility report summary.
   - Build instructions and installation instructions.
   - List of implemented features (UI, WebSocket, Models, Settings) and incomplete features (Live Telemetry Integration - pending official API/Mod support).

## 8. Confirmed Facts & Limitations
- **Fact:** Package `com.StefMorojna.SpaceflightSimulator`, v1.6.00.22 (May 2026) [1][2].
- **Fact:** Mobile modding is asset-only (`.pack`, textures) [1][5].
- **Fact:** No executable code (`.dll`) or scripting API on Android [1][5].
- **Fact:** No official IPC/Telemetry API for companion apps [6].
- **Limitation:** Real-time game state (altitude, velocity) **cannot** be read by an external companion app on the official Android version without root or screen capture (both forbidden).
- **Recommendation:** Build the companion application as a **functional framework** ready for integration, with the **Game-Side Adapter** defined as a future component that would require either:
  - An official API update from Team Curiosity ✦.
  - A future modding interface that allows data export (currently not available).
  - **Note:** The specification requires building the application **first**. We will build the full UI, WebSocket, and Models, and explicitly state in the report and code comments that the **Game-Side Integration Component** is currently **architecturally defined but technically blocked** by the official game's lack of an external API, adhering strictly to the "Do NOT simulate direct game integration using fake telemetry" rule.

---

## Revised Application Architecture

```text
app/
├── src/main/java/com/sfsai/
│   ├── MainActivity.kt                 # Entry point, Navigation
│   ├── ui/
│   │   ├── dashboard/                  # Main Dashboard (Status, Scene, Diagnostics)
│   │   ├── monitor/                    # Live Flight Monitor (Telemetry placeholders)
│   │   ├── missions/                   # Future MVP 4/5 (Placeholders)
│   │   ├── settings/                   # Settings (WebSocket Address, Modes, Debug)
│   │   └── components/                 # Material 3 Cards, Theme (#0B1020, #7C5CFF, etc.)
│   ├── networking/
│   │   ├── WebSocketClient.kt          # Connection, Reconnection, JSON Serialization
│   │   └── MessageSerializer.kt        # Protocol v1 (Observation message)
│   ├── models/
│   │   ├── GameState.kt                # Scene (Main Menu, Rocket, Flight, Map, Unknown)
│   │   ├── Telemetry.kt                # Altitude, Velocity, Fuel (nullable, confidence)
│   │   └── Observation.kt              # Timestamp, Source, State
│   ├── storage/
│   │   └── SettingsRepository.kt       # SharedPreferences (WebSocket URL, Mode, Debug)
│   ├── vision/                         # STUB/INTERFACE (Per Spec: No screen capture main)
│   │   ├── GameRecognizer.kt           # Interface (Returns UNKNOWN or Scene if file-based)
│   │   └── HudRecognizer.kt            # Interface (Returns null telemetry)
│   ├── capture/                        # STUB/INTERFACE (Per Spec: No screen capture main)
│   │   └── MediaProjectionManager.kt   # Interface (Disabled/Stubbed)
│   ├── services/                       # STUB/INTERFACE
│   │   └── ScreenCaptureService.kt     # Disabled/Stubbed
│   └── utils/
│       └── Logger.kt                   # Debug logging
└── AndroidManifest.xml                 # Permissions (INTERNET, POST_NOTIFICATIONS, etc.)
```

## Conclusion
The feasibility investigation confirms that **genuine game-side integration** (reading live telemetry from `com.StefMorojna.SpaceflightSimulator` v1.6.00.22) is **currently not supported** by the official Android application due to the lack of an external API and the restriction on executable mod extensions. 

**MVP 1 will proceed by:**
1. Building the **complete Android AI Application** (UI, WebSocket, Models, Settings).
2. Implementing the **Communication Foundation** (WebSocket Client) ready for a future Game-Side Adapter.
3. Defining the **Game-Side Adapter** as an interface/stub, explicitly noting in the report and code that **live telemetry integration is pending** an official API or future modding support, and **strictly avoiding** fake telemetry or screen capture.
4. Providing all required deliverables (Project, README, Build Instructions, etc.).

---
**References:**
[1] r/SpaceflightSimulator on Reddit: Complete guide for mods on mobile (Jan 18, 2026) https://www.reddit.com/r/SpaceflightSimulator/comments/1ptvo61/complete_guide_for_mods_on_mobile/
[2] Spaceflight Simulator MOD APK v1.6.00.22 (Unlocked) - Apkmody (May 01, 2026) https://apkmody.mobi/games/spaceflight
[3] Spaceflight Simulator 1.5.9.15 APK Download - APKMirror (Jun 23, 2024) https://www.apkmirror.com/apk/stefo-mai-morojna/spaceflight-simulator/spaceflight-simulator-1-5-9-15-release/spaceflight-simulator-1-5-9-15-2-android-apk-download/
[4] Spaceflight Simulator (MOD: Unlocked) APK v1.6.00.22 - APKVision (May 02, 2026) https://apkvision.org/games/strategy/spaceflight-simulator-17564/
[5] r/SpaceflightSimulator on Reddit: How to download mods on mobile? (Dec 23, 2025) https://www.reddit.com/r/SpaceflightSimulator/comments/1pttxbx/how_to_download_mods_on_mobile/
[6] Exploring Inter-Process Communication in Android - Stackademic (Apr 22, 2024) https://blog.stackademic.com/exploring-inter-process-communication-in-android-4-ways-for-activities-and-services-to-communicate-if-running-on-different-processes
