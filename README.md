<div align="center">
  <img src="docs/auranova_logo.png" alt="AuraNova Logo" width="100"/>

  <h1>NeoBank UI</h1>
  <p><strong>Fintech Interface Showcase built with Kotlin and Jetpack Compose for Android.</strong></p>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-blue.svg?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-purple.svg?style=flat-square&logo=android)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Android](https://img.shields.io/badge/Platform-Android-black?style=flat-square&logo=android)]()
[![CI/CD](https://img.shields.io/badge/Build-Passing-brightgreen.svg?style=flat-square&logo=githubactions)]()

<p align="center">
  <a href="https://github.com/JastinBolanos/NeoBankUI-KMP/releases/download/v1.2.0/NeoBankUI.apk">
    <img src="https://img.shields.io/badge/Download-APK%20Android-success?style=for-the-badge&logo=android&logoColor=white" alt="Descargar APK">
  </a>
</p>
</div>

---

## Overview

**AuraNova** is an exploratory financial client interface built with **Kotlin** and **Jetpack Compose** for **Android**. The project demonstrates how modern declarative UI principles and clean component hierarchies can deliver a responsive, premium native experience on mobile devices.

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

* **Core Framework:** Kotlin and Jetpack Compose targeting Android.
* **Architecture:** Clean Architecture with Feature-driven organization and structured State Hoisting.
* **UI/UX Design:** High-fidelity Dark Mode utilizing native Compose canvas modifiers for translucent surfaces, soft shadows, and clean typographic hierarchies.
* **Navigation:** Back-stack handling implemented via Android's `BackHandler` to manage Android system back events consistently.
* **Gesture Interactions:** Smooth gesture handling (`detectHorizontalDragGestures`) paired with coordinated entrance animations (fade-in and slide-up).
* **Continuous Integration:** Automated UI component compilation and Gradle build validation via GitHub Actions.

---

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/JastinBolanos/NeoBank-UI.git
   cd NeoBank-UI
   ```

2. **For Android:** Open the project in Android Studio, select the `app` configuration, and press *Run*.
