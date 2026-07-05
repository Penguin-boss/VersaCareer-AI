package com.example.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiSkillDto(
  val name: String,
  val level: String // "ADVANCED", "INTERMEDIATE", "BEGINNER"
)

@JsonClass(generateAdapter = true)
data class GeminiMissingSkillDto(
  val name: String,
  val priority: String, // "HIGH", "MEDIUM", "LOW"
  val category: String,
  val resourcesLink: String = "https://roadmap.sh"
)

@JsonClass(generateAdapter = true)
data class GeminiProjectDto(
  val title: String,
  val description: String,
  val skills: List<String>,
  val duration: String,
  val difficulty: String // "Easy", "Medium", "Hard"
)

@JsonClass(generateAdapter = true)
data class GeminiRoadmapDto(
  val week: Int,
  val title: String,
  val description: String,
  val status: String // "COMPLETED", "IN_PROGRESS", "LOCKED"
)

@JsonClass(generateAdapter = true)
data class GeminiAnalysisResultDto(
  val resumeScore: Int = 0,
  val atsScore: Int = 0,
  val technicalScore: Int = 0,
  val experienceScore: Int = 0,
  val projectScore: Int = 0,
  val jobReadiness: Int = 0,
  val futureEmployability: Int = 0,
  val currentSkills: List<GeminiSkillDto> = emptyList(),
  val missingSkills: List<GeminiMissingSkillDto> = emptyList(),
  val strengths: List<String> = emptyList(),
  val weaknesses: List<String> = emptyList(),
  val recommendedProjects: List<GeminiProjectDto> = emptyList(),
  val roadmap: List<GeminiRoadmapDto> = emptyList()
)

data class DimensionDto(
  val name: String,
  val value: Float,
  val scoreLabel: String,
  val description: String
)

data class TrendDto(
  val techName: String,
  val trendLabel: String,
  val percentChange: String,
  val direction: String // "UP", "STABLE", "DOWN"
)
