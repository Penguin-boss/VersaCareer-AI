package com.example.data

enum class UploadStatus {
  IDLE,
  UPLOADING,
  ANALYZING,
  COMPLETED
}

data class ResumeState(
  val fileName: String = "No file selected",
  val fileSize: String = "",
  val progress: Float = 0f,
  val status: UploadStatus = UploadStatus.IDLE,
  val fileUri: String? = null,
  val extractedText: String = ""
)

data class SkillItem(
  val name: String,
  val level: String // "ADVANCED", "INTERMEDIATE", "BEGINNER"
)

enum class Priority {
  HIGH,
  MEDIUM,
  LOW
}

data class MissingSkillItem(
  val name: String,
  val priority: Priority,
  val category: String, // "Cloud", "Programming", "Database", "Tools", etc.
  val isLearning: Boolean = false,
  val resourcesLink: String = "https://roadmap.sh"
)

enum class MilestoneStatus {
  COMPLETED,
  IN_PROGRESS,
  LOCKED
}

data class Milestone(
  val week: Int,
  val title: String,
  val description: String,
  val status: MilestoneStatus
)

data class BadgeItem(
  val title: String,
  val dateEarned: String,
  val isLocked: Boolean,
  val iconEmoji: String
)

data class ActivityHistory(
  val fileName: String,
  val date: String,
  val score: Int
)

data class ResumeAnalysisState(
  val isRealSelected: Boolean = false,
  val isAnalysisAvailable: Boolean = false,
  val isError: Boolean = false,
  val fileName: String = "",
  val fileSize: String = "",
  val overallScore: Int = 0,
  val technicalSkillsScore: Int = 0,
  val atsCompatibilityScore: Int = 0,
  val marketRelevanceScore: Int = 0,
  val projectQualityScore: Int = 0,
  val strengths: List<String> = emptyList(),
  val weaknesses: List<String> = emptyList(),
  val suggestions: List<String> = emptyList(),
  
  // Custom analytical fields populated from Gemini Analysis
  val jobReadinessScore: Int = 0,
  val futureEmployabilityScore: Int = 0,
  val portfolioStrengthScore: Int = 0,
  val recommendedProjects: List<GeminiProjectDto> = emptyList(),
  val dimensions: List<DimensionDto> = emptyList(),
  val trends: List<TrendDto> = emptyList(),
  val suitableRolesText: String = ""
)
