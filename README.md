# MindNotes 🧠

MindNotes is a sophisticated, minimalist Android application designed for mindful journaling and personal reflection. It allows users to capture thoughts, track moods, and categorize their entries (with a special focus on Stoicism and personal analysis), all while providing a beautiful, animated user experience.

## ✨ Features

- **Rich Note Taking**: Create and edit notes with titles, detailed content, and category tags.
- **Mood Tracking**: Every note is associated with a mood (1-5 scale), helping you visualize your emotional well-being over time.
- **Category System**: Organize your thoughts using built-in categories:
  - 🏺 Stoicism
  - 📝 Daily Summary
  - 🌿 Thankfulness
  - 🔍 Analysis
  - 📌 Other
- **Visual Calendar**: A dedicated calendar view that colors each day based on your average mood, allowing for quick emotional retrospection.
- **Dynamic Themes**: Support for multiple visual themes to suit your style.
- **Fluid UI**: Built with Jetpack Compose, featuring smooth transitions and a floating navigation bar for effortless interaction.
- **Modern Architecture**: Built using Clean Architecture principles and MVVM for high maintainability and testability.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/compose/)
- **Asynchronous Work**: Kotlin Coroutines & Flow

## 🏗 Architecture

The project follows the **Clean Architecture** pattern, divided into three main layers:

1.  **Data Layer**: Handles data persistence using Room and manages repositories.
2.  **Domain Layer**: Contains business logic (Use Cases) and pure Kotlin models.
3.  **Presentation Layer**: MVVM pattern with Jetpack Compose for the UI.

### Project Structure
```text
com.ross.mindnotes/
├── data/           # Repository implementations, Room Entities, DAOs
├── di/             # Hilt Dependency Injection modules
├── domain/         # Business logic: Use Cases & Models
└── presentation/   # UI: Screens, ViewModels, Components, Themes
```

## 🚀 Getting Started

1.  Clone the repository.
2.  Open the project in **Android Studio (Ladybug or newer)**.
3.  Ensure you have the latest **Android SDK** and **JDK 11+** installed.
4.  Build and run the app on an emulator or physical device (Min SDK 34).

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details (or add your own license).
