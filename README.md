# 🚗 Gyan AAD Hub (Android Auto App Downloader & Safe Installer)

**A modern Android Auto App Hub, Downloader & Installer.**  
Copyright (C) 2026 **GyanXD25** • Developer Portfolio: [gyan-portfolio-coral.vercel.app](https://gyan-portfolio-coral.vercel.app)  
Licensed under **GNU General Public License v3.0 (GPL-3.0)**  
*Based on KingInstaller core installation engine by fcaronte, jen94 & Rikj000.*

---

## ⚠️ Important Driver Safety & Legal Notice

> [!CAUTION]
> **SAFETY FIRST:** Operating media streaming, video players, web browsers, or screen mirroring apps while driving is hazardous, illegal in most jurisdictions, and increases the risk of serious accidents.
> 
> These applications are intended **exclusively for passengers or when the vehicle is safely parked**. Gyan AAD Hub and its contributors assume no responsibility or liability for misuse. Always prioritize road safety and adhere strictly to local traffic regulations.

---

## 🌟 Why Gyan AAD Hub?

Regular users who want multimedia, utility, and screen mirroring tools on their car dashboard usually face multiple hurdles:
1. **GitHub release navigation is difficult** for non-technical users (choosing CPU architectures, finding APKs among asset lists).
2. **Sketchy third-party APK mirrors** often distribute repackaged versions with unwanted adware or malware.
3. **AAAD** enforces an artificial limit (1 download per month) unless a paid upgrade is purchased.

**Gyan AAD Hub** bridges this gap:
* 🛡️ **Verified Upstream Sources:** Every app is downloaded directly from its official upstream GitHub repository releases.
* 📦 **Version Selector:** Choose specific previous versions or grab the latest release on demand.
* ⚡ **Automatic Installer Spoofing:** Sideloads apps with Google Play Store (`com.android.vending`) metadata so they appear properly in Android Auto.
* 🔓 **100% Free & Unlimited:** No download limits, timers, or paywalls.
* 🎨 **Cyberpunk Palette:** Styled with GyanXD25's signature red-blue-gold & orange-pink palette.
* ☁️ **Cloud CI/CD (GitHub Actions):** Automatic build and release in the cloud—no local Android Studio required.

---

## 📱 Curated App Catalog

All downloads link directly to the developers' official release tags:

| App | Category | Description | Upstream Source |
|---|---|---|---|
| **Fermata Auto** | Video & Media | YouTube, local audio/video, IPTV playlists (m3u/m3u8), and built-in web browser | [AndreyPavlenko/Fermata](https://github.com/AndreyPavlenko/Fermata) |
| **CarStream** | Video Streaming | Watch YouTube videos on your vehicle dashboard | [theiostream/CarStream](https://github.com/theiostream/CarStream) |
| **Screen2Auto** | Screen Mirroring | Full smartphone screen mirroring with multi-touch and DPI scaling | Inceptive |
| **AA Mirror Plus** | Screen Mirroring | Multi-touch gesture controls, aspect ratio fitting, and auto-rotation | [Rikj000/AAMirror](https://github.com/Rikj000/AAMirror) |
| **AA Passenger** | Passenger Media | Control audio, video, and media playback from passenger seats | [croconaut/passenger](https://github.com/croconaut/android-auto-passenger) |
| **Fermata Control** | Utilities | Steering wheel key interception and notification media controls | [AndreyPavlenko/Fermata](https://github.com/AndreyPavlenko/Fermata) |
| **AA Browser** | Web Browser | Full Chromium-based browser with touch navigation | [fcaronte/AABrowser](https://github.com/fcaronte/AABrowser) |
| **IPcarTV** | Live TV | Live DVB-T and IPTV streaming inside Android Auto | IPcarTV |

---

## 🛠️ Compatibility & Installation Methods

Android Auto restricts non-Play Store apps from showing on car screens. Gyan AAD Hub provides three installation workflows depending on your device setup:

### 1️⃣ Method 1: Classic Spoofing (Recommended Default)
* **Best for:** Most stock Android devices and custom ROMs (Android 10 through 16).
* **How it works:** Uses the system `PackageInstaller` session with `com.android.vending` originating flags.
* **Requirements:** No root or extra apps needed. Enable "Install unknown apps" when prompted.

### 2️⃣ Method 2: Shizuku Mode
* **Best for:** Devices where the Classic method doesn't register the Play Store flag properly.
* **How it works:** Leverages elevated ADB permissions through a background IPC service to invoke shell-level package management.
* **Requirements:** Install [Shizuku](https://shizuku.rikka.app) or [Shizuku-Next](https://github.com/rushiranpise/Shizuku-Next). Authorize Gyan AAD Hub inside the Shizuku app.

### 3️⃣ Method 3: Root Mode (`su`)
* **Best for:** Devices with heavily restricted OEM skins (specifically **Xiaomi, POCO, and Redmi** running MIUI or HyperOS, which strictly block third-party installer metadata).
* **How it works:** Direct shell execution via `su` to manipulate the package manager database (`pm install -i com.android.vending`).
* **Requirements:** Magisk, KernelSU, or APatch root access.

---

## 🔒 Security & Signing Configuration

To ensure supply-chain integrity:
* Release signing credentials are **never stored in source control**.
* The GitHub Actions CI pipeline supports protected repository secrets:
  * `RELEASE_KEYSTORE_BASE64`: Base64-encoded release `.jks` file
  * `RELEASE_KEYSTORE_PASSWORD`: Keystore password
  * `RELEASE_KEY_ALIAS`: Key alias
  * `RELEASE_KEY_PASSWORD`: Key password
* For local development, copy `signing.properties.example` to `signing.properties` (which is excluded by `.gitignore`). If no keys are configured, debug signing is used automatically so builds never fail.

---

## 📄 License & Attribution

* **Maintainer / Design:** [GyanXD25](https://gyan-portfolio-coral.vercel.app)
* **Core Installer Engine:** Based on KingInstaller by [fcaronte](https://github.com/fcaronte), [jen94](https://github.com/jen94), and [Rikj000](https://github.com/Rikj000).
* **License:** [GNU General Public License v3.0 (GPL-3.0)](LICENSE)
