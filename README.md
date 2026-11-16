# Impahla 🎁

**Impahla** is a modern Android application built with **Jetpack Compose** and **Room Database** designed to help users track, manage, and organize contributions and gifts (e.g. for weddings, kitchen parties, graduations, and fundraising events).

---

## 🌟 Key Features

- **Contribution Management**: Add, edit, view, and delete contribution records with support for Cash, Physical Gifts, or Both.
- **Fulfillment Tracking**: Categorize contributions as **Received (Handed over)** or **Promised (Pledged)**.
- **Robust Input Validation & Constraints**:
  - **Contributor Names**: Automatically filters out numbers and special symbols on input and validates against name standards (supporting letters, spaces, hyphens, apostrophes, and periods).
  - **Cash Amounts & Gift Descriptions**: Validates numeric input and required fields before saving.
- **Photo Attachments**: Capture or attach photos to contribution records.
- **Search & Filter**: Instantly search contributions by contributor name or gift description.
- **Dashboard & Statistics**: Real-time overview of total received cash, promised cash, unique contributor count, and outstanding physical gifts.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/).
- **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF) using `StateFlow`.
- **Local Persistence**: [Room Database](https://developer.android.com/topic/libraries/architecture/room) with KSP.
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose).
- **Asynchronous Operations**: Kotlin Coroutines & Flow.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/).

---

## 👥 Team Members & Allocation

| Name | Student ID | Responsibilities / Roles |
| :--- | :--- | :--- |
| **Robert Muntanga** | 2420908 | UI & Navigation; Camera co-owner |
| **Wongani Miyanza** | 2420907 | Add/edit forms & validation; Room co-owner |
| **Oliver Kakiungu** | 2300176 | Contributors list & filtering; Room co-owner |

---

## 📂 Project Structure

```text
com.example.impahla/
├── data/
│   ├── local/          # Room Database, DAOs, Entities, and Type Converters
│   └── repository/     # Repository layer abstracting data sources
├── model/              # Enums and data models (ContributionStatus, ContributionType)
├── ui/
│   ├── add_edit/       # Add & Edit contribution screen and ViewModel
│   ├── components/     # Reusable UI components (Cards, Photo Picker, Status Badge)
│   ├── dashboard/      # Dashboard screen and statistics ViewModel
│   ├── detail/         # Detail view screen and ViewModel
│   ├── list/           # Contribution list screen and ViewModel
│   ├── navigation/     # NavGraph setup
│   └── theme/          # Material 3 Theme, Colors, and Typography
└── util/               # Utility functions (FileUtil, etc.)
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Koala / Ladybug or newer recommended)
- **Android SDK** (Compile SDK 35, Min SDK 24)
- **JDK 11** or higher

### Running the App
1. Clone or open the repository in Android Studio.
2. Sync project with Gradle files (`File > Sync Project with Gradle Files`).
3. Select an emulator or connected Android device.
4. Click **Run** (`Shift + F10`) to build and launch the application.

---

## 🧪 Testing

Run local unit tests via Gradle:
```bash
./gradlew :app:testDebugUnitTest
```
