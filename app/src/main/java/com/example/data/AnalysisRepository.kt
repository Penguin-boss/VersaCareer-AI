package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnalysisRepository(private val context: Context) {

  suspend fun analyzeResumeFile(uri: Uri, fileName: String, fileSize: String): AnalysisOutputData = withContext(Dispatchers.IO) {
    // 1. Extract and clean resume content
    val extractedText = ResumeParserService.parseResume(context, uri, fileName)
    
    // 2. Perform Gemini AI analysis
    val resultDto = GeminiAnalysisService.analyzeResume(extractedText)
    
    // 3. Map to Domain models and structure state
    val domainCurrentSkills = resultDto.currentSkills.map { dto ->
      SkillItem(
        name = dto.name,
        level = when (dto.level.uppercase()) {
          "ADVANCED" -> "ADVANCED"
          "INTERMEDIATE" -> "INTERMEDIATE"
          else -> "BEGINNER"
        }
      )
    }

    val domainMissingSkills = resultDto.missingSkills.map { dto ->
      MissingSkillItem(
        name = dto.name,
        priority = when (dto.priority.uppercase()) {
          "HIGH" -> Priority.HIGH
          "MEDIUM" -> Priority.MEDIUM
          else -> Priority.LOW
        },
        category = dto.category,
        isLearning = false,
        resourcesLink = dto.resourcesLink.ifEmpty { "https://roadmap.sh" }
      )
    }

    val domainRoadmap = resultDto.roadmap.map { dto ->
      Milestone(
        week = dto.week,
        title = dto.title,
        description = dto.description,
        status = when (dto.status.uppercase()) {
          "COMPLETED" -> MilestoneStatus.COMPLETED
          "IN_PROGRESS" -> MilestoneStatus.IN_PROGRESS
          else -> MilestoneStatus.LOCKED
        }
      )
    }

    // Prepare custom evaluative sub-dimensions for the Job Readiness screen
    val dimensions = listOf(
      DimensionDto("Technical Skill Alignment", resultDto.technicalScore / 100f, "${resultDto.technicalScore}%", "Identified programming, API and algorithmic competencies."),
      DimensionDto("Project Experience Proof", resultDto.projectScore / 100f, "${resultDto.projectScore}%", "Demonstrated portfolio metrics and codebase evidence."),
      DimensionDto("ATS File Compliance", resultDto.atsScore / 100f, "${resultDto.atsScore}%", "Clean text structure matching system parser keywords."),
      DimensionDto("Cloud & DevOps Exposure", resultDto.experienceScore / 100f, "${resultDto.experienceScore}%", "Understanding of modern deployments, actions or containers.")
    )

    // Prepare custom growth trajectories for the Future Employability screen
    val missingTechName = resultDto.missingSkills.firstOrNull()?.name ?: "AWS Cloud, Docker"
    val currentTechName = resultDto.currentSkills.firstOrNull()?.name ?: "Kotlin, Compose"
    
    val trends = listOf(
      TrendDto("Cloud DevOps ($missingTechName)", "High Trend", "+42% demand", "UP"),
      TrendDto("Modern Core ($currentTechName)", "Stable Trend", "+15% demand", "STABLE"),
      TrendDto("Legacy Architectures", "Decline Trend", "-8% demand", "DOWN")
    )

    // Generate dynamic suitable roles copy
    val missingStrParts = resultDto.missingSkills.take(3).map { it.name }
    val rolesCopy = if (missingStrParts.isNotEmpty()) {
      "Your resume exhibits strong core foundations with ${resultDto.resumeScore}% matching competency, but encounters some gap challenges under: ${missingStrParts.joinToString(", ")}."
    } else {
      "Your resume exhibits pristine alignment (${resultDto.resumeScore}%) to target software development roles with zero core gap challenges."
    }

    val analysisState = ResumeAnalysisState(
      isRealSelected = true,
      isAnalysisAvailable = true,
      isError = false,
      fileName = fileName,
      fileSize = fileSize,
      overallScore = resultDto.resumeScore,
      technicalSkillsScore = resultDto.technicalScore,
      atsCompatibilityScore = resultDto.atsScore,
      marketRelevanceScore = resultDto.experienceScore,
      projectQualityScore = resultDto.projectScore,
      strengths = resultDto.strengths,
      weaknesses = resultDto.weaknesses,
      suggestions = emptyList(), // Can hold additional insights
      
      jobReadinessScore = resultDto.jobReadiness,
      futureEmployabilityScore = resultDto.futureEmployability,
      portfolioStrengthScore = resultDto.projectScore,
      recommendedProjects = resultDto.recommendedProjects,
      dimensions = dimensions,
      trends = trends,
      suitableRolesText = rolesCopy
    )

    return@withContext AnalysisOutputData(
      analysisState = analysisState,
      currentSkills = domainCurrentSkills,
      missingSkills = domainMissingSkills,
      roadmap = domainRoadmap
    )
  }
}

data class AnalysisOutputData(
  val analysisState: ResumeAnalysisState,
  val currentSkills: List<SkillItem>,
  val missingSkills: List<MissingSkillItem>,
  val roadmap: List<Milestone>
)
