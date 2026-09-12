# 🏆 ApexArena: Competitive Tournament Platform

ApexArena is a production-grade, high-performance native Android application engineered for esports tournaments and competitive gaming matches. Built using **Kotlin**, **Jetpack Compose**, **Material 3**, and **Supabase**.

---

## ⚡ Key Highlights
* **UI/UX Pro Max Design System:** Obsidian Esports Neon theme (`#0F0F23`, `#7C3AED`, `#F43F5E`) with tactile feedback, zero-slop animations, and accessible touch targets.
* **Modern Android Architecture:** Presentation → ViewModel → Domain/UseCase → Repository → Supabase / Local Storage.
* **Complete Screen Flows:**
  * 🔐 **Authentication:** Email Login, Sign Up, Profile Initialization, Session Restore.
  * 🏠 **Dashboard:** Featured banners, tier division badges, live and upcoming tournaments.
  * 🎯 **Tournament Arena:** Real-time search, format filtering (Solo, Squad), prize pool details, registration flow, and official rules.
  * 🌳 **Knockout Bracket Viewer:** Interactive tree with round columns, live scores, and progression tracking.
  * ⚔️ **Match Center:** Upcoming, Live, and Completed match tracking with scoreboards and dispute filing.
  * 👑 **Global Leaderboard:** Real-time ladder with player tiers (Diamond, Master, Legend), win-rates, and points.
  * 🔔 **Notification Center:** Match alerts, tournament registration confirmations, and tier rewards.
  * 👤 **Gladiator Profile & Settings:** Match record statistics (Wins/Losses/Win Rate), in-game UID editor, and OLED dark mode.
* **Database & Security:** Supabase PostgreSQL 15 schema with 7 core tables, foreign keys, and strict Row Level Security (RLS) policies.
* **Automated CI/CD:** GitHub Actions workflow (`.github/workflows/android.yml`) for automated building and downloadable debug `.apk` artifacts.

---

## 📂 Project Structure
```
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/apex/arena/
│       │   │   ├── core/ (theme, components, navigation, network)
│       │   │   ├── domain/ (models, repository contracts)
│       │   │   ├── data/ (repository implementations, Supabase integrations)
│       │   │   └── presentation/ (auth, home, tournaments, matches, leaderboard, notifications, profile)
│       │   └── res/ (values, themes, strings)
│       └── test/ (Unit test suite for ViewModels and Repositories)
├── gradle/
│   └── libs.versions.toml (Gradle Version Catalog)
├── supabase/
│   └── migrations/ (PostgreSQL schema & RLS policies)
├── docs/
│   ├── ui/ (Design System Specification)
│   └── backend/ (Database architecture)
├── .github/workflows/
│   └── android.yml (Automated CI/CD pipeline)
├── push.py (GitHub deployment script)
└── BUILD_REPORT.md
```

---

## 🚀 Getting Started & Local Run

### Prerequisites
* Android Studio Ladybug / Meerkat or later
* JDK 17
* Android SDK 35

### Running the App
1. Open the project in Android Studio.
2. Sync Gradle files.
3. Select an emulator (API 26+) or physical device.
4. Press **Run (Shift + F10)**.

### GitHub Push & Cloud APK Build
To push changes to GitHub and trigger automatic cloud APK compilation:
```bash
python3 push.py
```
After pushing, navigate to **Actions** in your GitHub repository to download the generated `.apk` artifact directly!
