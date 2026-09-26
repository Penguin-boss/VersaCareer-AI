# VersaCareer-AI

> AI tool to upskill profile.

A smart profile-upskilling app built to help users improve their professional readiness through AI-assisted analysis, skill discovery, and personalized growth recommendations.

## Overview

VersaCareer-AI is designed to help users understand where they stand in their career journey and what they need to improve next. The app focuses on turning a raw profile into a stronger, job-ready identity by identifying strengths, skill gaps, and actionable recommendations.

This project is built in Kotlin and is structured for Android app development, making it suitable for local development and future extension into richer AI-powered career guidance workflows.

## Features

- AI-powered profile analysis
- Skill gap identification
- Personalized upskilling suggestions
- Career-focused recommendations and roadmap guidance
- Clean Android/Kotlin app structure
- Local development setup with Android Studio
- Environment configuration for API keys

## Tech Stack

- Kotlin
- Android SDK
- Gradle
- Gemini API integration (configured through `.env`)
- Android Studio

## Repository Information

- Repository: `Penguin-boss/VersaCareer-AI`
- Primary Language: Kotlin
- Description: `AI tool to upskill profile`

## Prerequisites

Before running the project locally, make sure you have:

- Android Studio installed
- JDK 17 or newer recommended
- Android SDK configured
- A Gemini API key

## Run Locally

1. Open Android Studio.
2. Select **Open** and choose the directory containing this project.
3. Allow Android Studio to finish importing the project and fixing any compatibility issues.
4. Create a file named `.env` in the project root and set your Gemini API key:

   ```env
   GEMINI_API_KEY=your_api_key_here
   ```

   See `.env.example` for the expected format.
5. In the app-level `build.gradle.kts`, remove the line:

   ```kotlin
   signingConfig = signingConfigs.getByName("debugConfig")
   ```

6. Run the app on an emulator or physical device.

## Project Structure

```text
VersaCareer-AI/
├── app/
│   ├── src/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── .env.example
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
└── local.properties
```

## Configuration

This project uses environment-based configuration for API access.

- `.env.example` contains sample environment values.
- The application expects a `GEMINI_API_KEY` value for AI functionality.
- If needed, configure additional platform or build-specific settings in Android Studio or Gradle files.

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Test your work locally.
5. Open a pull request with a clear summary.

## License

This project does not currently list a license in the repository metadata provided to the assistant, so please check the repository files for the exact licensing terms before production use or redistribution.

## Support

For project questions or issues, use the repository's GitHub Issues page.

## Status

This repository is currently focused on enabling AI-assisted profile upskilling and professional growth workflows.

---

Built with Kotlin for Android and designed to help users level up their career profile with AI guidance.
