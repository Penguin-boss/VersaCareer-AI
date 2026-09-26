# VersaCareer-AI

<div align="center">

  <img src="https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Android-Ready-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/AI-UpSkill-FF6B6B?style=for-the-badge" alt="AI UpSkill" />

  <h3>AI-powered career upskilling for better profiles, smarter growth, and stronger opportunities.</h3>

</div>

VersaCareer-AI is an intelligent upskilling platform designed to help individuals improve their professional profile through AI-driven recommendations, skill analysis, and focused career growth guidance. The app helps users identify strengths, detect gaps, and unlock a more competitive path toward career advancement.

## Why VersaCareer-AI?

Modern career growth is no longer just about experience — it's about visibility, readiness, and continuous improvement. VersaCareer-AI helps users move beyond static resumes and generic profiles by turning career data into actionable insights.

Whether you're aiming to:

- sharpen your professional profile
- identify missing skills
- discover the right learning path
- improve your market readiness
- build momentum toward better opportunities

VersaCareer-AI gives you the tools to evolve with clarity and purpose.

## Key Features

- AI-based profile analysis
- Skill gap detection
- Personalized upskilling recommendations
- Career growth guidance
- Professional profile optimization suggestions
- Clean, modern Android experience
- Kotlin-first architecture
- Environment-based API configuration

## Tech Stack

- Kotlin
- Android SDK
- Gradle
- Gemini API integration
- Android Studio
- Dependency management via Gradle

## Project Vision

This project is built around the idea that career growth should be measurable, intelligent, and personalized. Instead of treating upskilling as a generic process, VersaCareer-AI focuses on turning user profiles into strategic assets by identifying what matters most in the current job market.

## Screens and Experience

The app is designed to provide a streamlined user journey:

1. Profile setup
2. AI-based analysis
3. Skill and gap discovery
4. Personalized recommendations
5. Continuous learning and improvement

## Repository Overview

- Repository: `Penguin-boss/VersaCareer-AI`
- Primary language: `Kotlin`
- Project theme: AI-powered professional upskilling
- Description: `AI tool to upskill profile`

## Prerequisites

Before running the project locally, ensure you have:

- Android Studio installed
- JDK 17 or newer recommended
- Android SDK configured
- A valid Gemini API key
- Internet access for API-based features

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Penguin-boss/VersaCareer-AI.git
cd VersaCareer-AI
```

### 2. Configure your environment

Create a `.env` file in the project root and add your API key:

```env
GEMINI_API_KEY=your_api_key_here
```

If a sample file exists, use it as a reference:

```bash
ls -a
cat .env.example
```

### 3. Open in Android Studio

- Open Android Studio
- Choose `Open` and select the project folder
- Let Gradle sync and resolve dependencies

### 4. Fix local build configuration if needed

In the app-level `build.gradle.kts`, remove the following line when required:

```kotlin
signingConfig = signingConfigs.getByName("debugConfig")
```

### 5. Run the app

Choose an emulator or connected device and launch the app:

```bash
./gradlew installDebug
```

Or run directly from Android Studio.

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
├── local.properties
└── LICENSE
```

## Configuration Notes

This project uses environment-based configuration for API access. Keep secrets out of source control and use `.env` or local developer configuration when needed.

## Typical User Flow

```text
User Profile
     ↓
AI Analysis
     ↓
Skill Gap Detection
     ↓
Targeted Recommendations
     ↓
Upskilling Roadmap
     ↓
Career Growth
```

## Development Workflow

Recommended workflow for contributors:

```bash
git checkout -b feature/your-feature-name
git add .
git commit -m "feat: add your feature"
git push origin feature/your-feature-name
```

Then open a pull request with a clear summary of the impact.

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch
3. Implement your change
4. Validate locally
5. Open a pull request

### Contribution Guidelines

- Keep code clean and readable
- Follow Kotlin style conventions
- Add tests when relevant
- Document new configuration or features clearly
- Keep commits descriptive and focused

## Roadmap

Planned improvements may include:

- richer AI profile scoring
- smarter recommendation engines
- broader skill tracking and analytics
- better user onboarding experience
- role-based career guidance
- resume and profile enhancement workflows

## License

This project should be checked against its repository license file before publishing, redistribution, or commercial use. If a license is present, it will typically be included in the root directory.

## Support

For questions, issues, or feedback:

- GitHub Issues: https://github.com/Penguin-boss/VersaCareer-AI/issues
- Repository: https://github.com/Penguin-boss/VersaCareer-AI

## Status

VersaCareer-AI is focused on helping users become more career-ready through intelligent profile improvement and personalized growth strategies.

---

Built with Kotlin and designed to turn potential into progress.

