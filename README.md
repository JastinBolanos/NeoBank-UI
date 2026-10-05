<div align="center">
  <img src="docs/auranova_logo.png" alt="AuraNova Logo" width="100"/>

  <h1>NeoBank UI KMP</h1>
  <p><strong>Fintech Interface Showcase built with Kotlin Multiplatform.</strong></p>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-blue.svg?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-purple.svg?style=flat-square&logo=android)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![iOS & Android](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-black?style=flat-square&logo=apple)]()
[![CI/CD](https://img.shields.io/badge/Build-Passing-brightgreen.svg?style=flat-square&logo=githubactions)]()

<p align="center">
  <a href="https://github.com/JastinBolanos/NeoBankUI-KMP/releases/download/v1.2.0/NeoBankUI.apk">
    <img src="https://img.shields.io/badge/Download-APK%20Android-success?style=for-the-badge&logo=android&logoColor=white" alt="Descargar APK">
  </a>
</p>
</div>

---

## Overview

**AuraNova** is an exploratory financial client interface built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. The project demonstrates how modern declarative UI principles and clean component hierarchies can deliver a responsive, premium native experience on mobile devices.

All screens are rendered with 100% native UI code (Compose), without embedded WebViews or hybrid wrappers, ensuring smooth 120Hz performance and gesture responsiveness.

---

## App Preview

### 🔒 Core Banking & Privacy
> Simulated biometric authentication, layered translucent surfaces, and dynamic privacy toggles that obscure sensitive balance details.

| Biometric Auth | Dashboard View | Card Privacy |
| :---: | :---: | :---: |
| <img src="docs/01_biometric_login.png" width="220" alt="Login"/> | <img src="docs/03_home_titanium_banner.png" width="220" alt="Dashboard"/> | <img src="docs/06_cards_privacy.png" width="220" alt="Privacy"/> |

### 💸 Fluid Transactions & Custom UI
> Infinite promotional carousels built with `HorizontalPager`, custom numeric keypads, and structured transaction histories.

| Promotional Banner | Transaction History | Smart Transfer |
| :---: | :---: | :---: |
| <img src="docs/04_home_credit_banner.png" width="220" alt="Banner"/> | <img src="docs/07_transaction_history.png" width="220" alt="History"/> | <img src="docs/08_send_money_transfer.png" width="220" alt="Transfer"/> |

---

## Technical Highlights

* **Core Framework:** Kotlin Multiplatform and Compose Multiplatform targeting Android (Native) and iOS.
* **Architecture:** Clean Architecture with Feature-driven organization and structured State Hoisting.
* **UI/UX Design:** High-fidelity Dark Mode utilizing native Compose canvas modifiers for translucent surfaces, soft shadows, and clean typographic hierarchies.
* **Cross-Platform Navigation:** Back-stack handling implemented via Kotlin's `expect/actual` pattern (`KmpBackHandler`) to manage Android system back events consistently alongside iOS navigation gestures.
* **Gesture Interactions:** Smooth gesture handling (`detectHorizontalDragGestures`) paired with coordinated entrance animations (fade-in and slide-up).
* **Continuous Integration:** Automated UI component compilation and `xcodebuild` validation via macOS GitHub Actions.

---

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/JastinBolanos/NeoBankUI-KMP.git](https://github.com/JastinBolanos/NeoBankUI-KMP.git)
   cd NeoBankUI-KMP
   ```

2. **For Android:** Open the project in Android Studio, select the `composeApp` configuration, and press *Run*.

