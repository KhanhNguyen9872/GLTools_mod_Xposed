<div align="center">
  <img src="image/logo_gltools.jpg" alt="GLTools Mod Logo" width="120" />
  <h1>GLTools Mod Xposed</h1>
  <p><strong>Advanced Bypass for GLTools</strong> <br /> Unlock Premium features for GLTools.</p>
</div>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)]()
[![Root](https://img.shields.io/badge/root-required-red.svg)]()
[![Xposed](https://img.shields.io/badge/framework-LSPosed-blue.svg)]()
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)]()

</div>

## Introduction

**GLTools Mod Xposed** is a dedicated Xposed/LSPosed module designed to revive and enhance the original **GLTools** app (`com.superpaninbros.glng`). Due to the original API servers (`gltools.app`) being defunct, the app would crash or fail to verify purchases. This module intercepts network requests, injects offline configurations securely, and completely unlocks all Premium functionality without requiring the external companion app.

## Key Features

-   **Premium Unlock**:
    -   Bypasses the companion app checks flawlessly.
    -   Forces the internal `CoinsManager` to recognize the Premium status unconditionally.
    -   Automatically hides the "Get companion" button from the main navigation sidebar.
-   **Network API Bypass**:
    -   Intercepts connections to the dead `gltools.app` API.
    -   Injects a locally hardcoded `version.js` response to prevent crashes and ensure a stable launch.
-   **Custom UI & WebView Injection**:
    -   Intercepts internal WebView traffic (e.g., dead Twitter feeds or help pages).
    -   Injects custom branded HTML interfaces for a cleaner and modernized user experience.

## Original App
The modified module requires the original GLTools application. You can download the untouched APK right here from the repository:
**[Download GLTools_1.0_NG.apk](image/GLTools_1.0_NG.apk)**

## Requirements

-   **Rooted Android Device**
-   **LSPosed Framework** (Zygisk or Riru) installed and active.
-   Target App: **GLTools (`com.superpaninbros.glng`)** installed.

## Installation

1.  **Download Module**: Get the latest APK from the [Releases](https://github.com/KhanhNguyen9872/GLTools_mod_Xposed/releases) page.
2.  **Install App**: Install the original GLTools APK (link above) if you haven't already.
3.  **Install Module**: Install the **GLTools Mod Xposed** module APK on your device.
4.  **Activate in LSPosed**:
    -   Open **LSPosed Manager**.
    -   Enable **GLTools Mod Xposed**.
    -   Ensure the scope includes **GLTools** (`com.superpaninbros.glng`).
5.  **Apply Hooks**: Force Stop the GLTools app to allow LSPosed to hook into it.

## Usage

1.  Force Stop **GLTools** after activating the module.
2.  Open **GLTools**.
3.  The app should bypass all server checks, and Premium features will be fully unlocked instantly.
4.  Enjoy your optimized gaming configuration!

## Disclaimer

This software is provided "as is", without warranty of any kind. Modifying system behavior via Xposed carries inherent risks. The developer is **not responsible** for bootloops, data loss, or bricked devices. **Always backup your data** before installing system modules.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

**Nguyễn Văn Khánh** (KhanhNguyen9872)

-   GitHub: [@KhanhNguyen9872](https://github.com/KhanhNguyen9872)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.