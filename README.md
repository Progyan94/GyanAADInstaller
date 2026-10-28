# 🚗 Gyan AAD Hub (Android Auto App Downloader & Safe Installer)

**The All-in-One Android Auto App Hub, Downloader & Installer.**  
Copyright (C) 2026 **GyanXD25** • Developer Portfolio: [gyan-portfolio-coral.vercel.app](https://gyan-portfolio-coral.vercel.app)  
Licensed under **GNU General Public License v3.0 (GPL-3.0)**  
*Built upon the KingInstaller core installation engine by fcaronte, jen94 & Rikj000.*

---

## ⚠️ Important Driver Safety & Legal Notice

> [!CAUTION]
> **SAFETY FIRST:** Operating media streaming, video players, web browsers, or screen mirroring apps while driving is hazardous, illegal in most jurisdictions, and increases the risk of collisions.
> 
> These applications are intended **exclusively for passengers or when the vehicle is safely parked with the parking brake engaged**. Gyan AAD Hub and its contributors assume no responsibility or liability for misuse. Always prioritize road safety and adhere strictly to local traffic regulations.

---

## 🌟 Why Gyan AAD Hub?

Regular car owners wanting media, utility, and screen mirroring apps on their vehicle display face several obstacles:
1. **GitHub releases are confusing** for non-technical users (picking CPU architectures, finding APKs among asset lists).
2. **Sketchy third-party APK mirror sites** frequently distribute repackaged binaries with unwanted adware, telemetry, or outdated builds.
3. **AAAD** enforces an artificial limit (1 single download per month) unless an upgrade is paid for.

**Gyan AAD Hub** solves all of this:
* 🛡️ **Verified Upstream Sources:** Every app is downloaded directly from verified upstream repositories or dedicated Firebase storage endpoints.
* 📦 **Version Selector:** Choose specific stable or legacy versions on demand.
* ⚡ **Installer Spoofing:** Sideloads apps with Google Play Store (`com.android.vending`) metadata so Android Auto recognizes them.
* 🔓 **100% Free & Unlimited:** No download limits, no countdown timers, no paywalls.
* 🎨 **Cyberpunk Palette:** Styled with GyanXD25's signature red-blue-gold & orange-pink portfolio palette.
* ☁️ **Cloud CI/CD (GitHub Actions):** Automatic compiling and release in the cloud—no local Android Studio required.

---

## 🚗 Making Installed Apps Visible on Your Car Display

Android Auto enforces security restrictions that hide non-Play Store apps by default. Follow these steps to ensure all installed apps appear on your car display:

### Step 1: Enable Developer Mode in Android Auto
1. On your phone, open **Settings** ➔ search for **Android Auto** (or tap **Open Android Auto Settings** in Gyan AAD Hub).
2. Scroll to the very bottom to **Version and release notes**.
3. **Tap "Version" repeatedly (10 times)** until a toast says *"Developer mode enabled"*.

### Step 2: Enable "Unknown Sources"
1. In Android Auto settings, tap the **3 vertical dots menu** (top-right corner).
2. Select **Developer settings**.
3. Check the box for **"Unknown sources"**.

### Step 3: Check "Customize Launcher"
1. In Android Auto Settings, tap **Customize launcher**.
2. Ensure your newly installed apps (**Fermata Auto**, **CarStream**, **Screen2Auto**, etc.) are **checked and enabled** in the app list.

### Step 4: Android 14+ / HyperOS / OneUI Devices
> [!IMPORTANT]
> Starting with Android 14, Google blocked standard non-root `PackageInstaller` sessions from spoofing the Play Store installer tag.
> If you are running Android 14, 15, or 16, or an OEM skin like Xiaomi HyperOS / Samsung OneUI, **switch to the "Installer" tab in Gyan AAD Hub and enable the Shizuku Method**. Shizuku uses elevated ADB privileges to successfully install apps with the proper Play Store tag.

---

## 📱 Curated App Catalog (11 Verified Apps)

All apps come with built-in official icons and direct, verified download links:

| App | Category | Description | Source / Upstream |
|---|---|---|---|
| **Fermata Auto** | Video & Media | YouTube, local audio/video, IPTV playlists (m3u/m3u8), and media browser | [AndreyPavlenko/Fermata](https://github.com/AndreyPavlenko/Fermata) |
| **CarStream** | Video Streaming | Watch YouTube videos on your vehicle dashboard | theiostream |
| **Screen2Auto** | Screen Mirroring | Full smartphone screen mirroring with multi-touch and DPI scaling | Inceptive / annexhack |
| **AA Mirror Plus** | Screen Mirroring | Multi-touch gesture controls, aspect ratio fitting, and auto-rotation | [Rikj000/AAMirror](https://github.com/Rikj000/AAMirror) |
| **AAStream** | Screen Mirroring | High-framerate video and display streamer with low audio/video latency | shmykelsa |
| **AA Passenger** | Passenger Media | Control audio, video, and media playback from passenger seats | [croconaut/passenger](https://github.com/croconaut/android-auto-passenger) |
| **AA Browser** | Web Browser | Chromium-based browser with touch navigation and bookmarking | [kododake/AABrowser](https://github.com/kododake/AABrowser) |
| **Performance Monitor**| Vehicle Telemetry | Live boost pressure, oil & coolant temps, and g-meter gauges | [jilleb/mqb-pm](https://github.com/jilleb/mqb-pm) |
| **AATorque** | OBD2 Telemetry | Live OBD2 engine diagnostics from Torque Pro directly on your car screen | [agronick/aa-torque](https://github.com/agronick/aa-torque) |
| **Widgets for Auto** | Dashboard Widgets| Display home screen widgets directly inside Android Auto | [ns130291/widgets-for-auto](https://github.com/ns130291/widgets-for-auto) |
| **Nav2Contacts** | Navigation Utility | Navigate to contact addresses directly without manual retyping | [frankkienl/Nav2Contacts](https://github.com/frankkienl/Nav2Contacts) |

---

## 🛠️ Installation Methods in Gyan AAD Hub

1. **Classic Spoofing (Default):**
   * Uses system `PackageInstaller` with `com.android.vending` flags. Great for Android 10–13.
2. **Shizuku Mode (Recommended for Android 14+):**
   * Uses [Shizuku](https://shizuku.rikka.app) / [Shizuku-Next](https://github.com/rushiranpise/Shizuku-Next) via ADB permissions to guarantee installer spoofing on modern Android versions.
3. **Root Mode (`su`):**
   * For rooted devices (Magisk, KernelSU, APatch) running strict OEM skins like HyperOS or MIUI.

---

## 🔒 Security & Signing Configuration

To ensure supply-chain integrity:
* Release signing credentials are **never stored in source control**.
* The GitHub Actions CI pipeline uses protected repository secrets:
  * `RELEASE_KEYSTORE_BASE64`: Base64-encoded persistent 2048-bit RSA release keystore
  * `RELEASE_KEYSTORE_PASSWORD`: Keystore password
  * `RELEASE_KEY_ALIAS`: Key alias
  * `RELEASE_KEY_PASSWORD`: Key password
* Verified reproducible release artifacts with SHA-256 checksums published on the [Releases](https://github.com/Progyan94/GyanAADInstaller/releases) page.

---

## 📄 License & Attribution

* **Maintainer / Design:** [GyanXD25](https://gyan-portfolio-coral.vercel.app)
* **Core Installer Engine:** Based on KingInstaller by [fcaronte](https://github.com/fcaronte), [jen94](https://github.com/jen94), and [Rikj000](https://github.com/Rikj000).
* **License:** [GNU General Public License v3.0 (GPL-3.0)](LICENSE)
