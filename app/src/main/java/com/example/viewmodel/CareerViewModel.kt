package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CareerViewModel : ViewModel() {

  // Global states for real-time reactivity
  private val _resumeState = MutableStateFlow(ResumeState())
  val resumeState: StateFlow<ResumeState> = _resumeState.asStateFlow()

  private val _analysisState = MutableStateFlow(ResumeAnalysisState())
  val analysisState: StateFlow<ResumeAnalysisState> = _analysisState.asStateFlow()

  private val _currentSkills = MutableStateFlow<List<SkillItem>>(emptyList())
  val currentSkills: StateFlow<List<SkillItem>> = _currentSkills.asStateFlow()

  private val _missingSkills = MutableStateFlow<List<MissingSkillItem>>(emptyList())
  val missingSkills: StateFlow<List<MissingSkillItem>> = _missingSkills.asStateFlow()

  private val _roadmap = MutableStateFlow<List<Milestone>>(emptyList())
  val roadmap: StateFlow<List<Milestone>> = _roadmap.asStateFlow()

  private val _badges = MutableStateFlow<List<BadgeItem>>(emptyList())
  val badges: StateFlow<List<BadgeItem>> = _badges.asStateFlow()

  private val _history = MutableStateFlow<List<ActivityHistory>>(emptyList())
  val history: StateFlow<List<ActivityHistory>> = _history.asStateFlow()

  // General profile parameters (Initially empty/zero)
  var userName = MutableStateFlow("")
  var jobTitle = MutableStateFlow("")
  var totalScans = MutableStateFlow(0)
  var topScore = MutableStateFlow(0)

  init {
    loadPruningPrereq()
  }

  private fun loadPruningPrereq() {
    _currentSkills.value = emptyList()
    _missingSkills.value = emptyList()
    _roadmap.value = emptyList()
    _badges.value = listOf(
      BadgeItem("Resume Scanner", "Pending scan evaluation", true, "🔍"),
      BadgeItem("Milestone Master", "Unlock Week 2 Milestone", true, "🏆"),
      BadgeItem("Cloud Creator", "Unlock Week 4 Milestone", true, "☁")
    )
    _history.value = emptyList()
    userName.value = "Applicant Profile"
    jobTitle.value = "Candidate"
  }

  fun selectRealFile(context: android.content.Context, name: String, size: String, uriString: String) {
    viewModelScope.launch {
      // Step 1: Upload Resume
      _resumeState.value = ResumeState(
        fileName = name,
        fileSize = size,
        progress = 0.2f,
        status = UploadStatus.UPLOADING,
        fileUri = uriString
      )
      delay(300)
      _resumeState.value = _resumeState.value.copy(progress = 0.5f)
      delay(300)

      // Step 2 & 3: Extract & Store Resume Text
      _resumeState.value = _resumeState.value.copy(
        progress = 0.8f,
        status = UploadStatus.ANALYZING
      )
      delay(300)

      var extracted = ""
      var isSuccess = true
      var errMsg = ""
      try {
        val uri = android.net.Uri.parse(uriString)
        extracted = ResumeParserService.parseResume(context, uri, name)
      } catch (e: Exception) {
        isSuccess = false
        errMsg = e.localizedMessage ?: "File read or extraction formatting error."
      }

      if (!isSuccess) {
        _resumeState.value = ResumeState(
          fileName = name,
          fileSize = size,
          progress = 0.0f,
          status = UploadStatus.IDLE,
          fileUri = null,
          extractedText = ""
        )
        _analysisState.value = ResumeAnalysisState(
          isRealSelected = false,
          isAnalysisAvailable = false,
          isError = true,
          fileName = name,
          fileSize = size
        )
        android.widget.Toast.makeText(context, "Text Extraction Fail: $errMsg", android.widget.Toast.LENGTH_LONG).show()
        return@launch
      }

      _resumeState.value = _resumeState.value.copy(
        progress = 1.0f,
        status = UploadStatus.COMPLETED,
        extractedText = extracted
      )

      // Initialize analysis state as NOT available yet - will only open up when they click "Ready for Analysis"
      _analysisState.value = ResumeAnalysisState(
        isRealSelected = true,
        isAnalysisAvailable = false,
        fileName = name,
        fileSize = size,
        overallScore = 0,
        strengths = emptyList(),
        weaknesses = emptyList(),
        suggestions = emptyList()
      )

      totalScans.value += 1
    }
  }

  // Beautiful parsing tool that dynamically parses keywords from the user's uploaded resume!
  fun triggerAnalysis(context: android.content.Context) {
    viewModelScope.launch {
      val textStr = _resumeState.value.extractedText
      if (textStr.isBlank()) {
        android.widget.Toast.makeText(context, "No resume content to analyze.", android.widget.Toast.LENGTH_SHORT).show()
        return@launch
      }

      // Update states to trigger loading indicators on screens
      _resumeState.value = _resumeState.value.copy(status = UploadStatus.ANALYZING)
      
      _analysisState.value = _analysisState.value.copy(
        isRealSelected = true,
        isAnalysisAvailable = false,
        isError = false
      )

      try {
        val uri = android.net.Uri.parse(_resumeState.value.fileUri ?: "")
        val repository = AnalysisRepository(context)
        
        val output = repository.analyzeResumeFile(
          uri = uri,
          fileName = _resumeState.value.fileName,
          fileSize = _resumeState.value.fileSize
        )

        // Populate state values with real structured intelligence
        _analysisState.value = output.analysisState
        _currentSkills.value = output.currentSkills
        _missingSkills.value = output.missingSkills
        _roadmap.value = output.roadmap

        // Unlock first badge
        _badges.value = _badges.value.map {
          if (it.title == "Resume Scanner") it.copy(isLocked = false, dateEarned = "Scanned successfully") else it
        }

        // Sync visual profile details
        topScore.value = output.analysisState.overallScore
        val parsedName = extractNameHelper(textStr)
        userName.value = if (parsedName.isNotEmpty()) parsedName else "Applicant Profile"
        jobTitle.value = if (output.currentSkills.isNotEmpty()) "${output.currentSkills.first().name} Specialist" else "Senior Candidate"

        // Keep history updated
        val activity = ActivityHistory(
          fileName = _resumeState.value.fileName,
          date = "Evaluated successfully",
          score = output.analysisState.overallScore
        )
        _history.value = listOf(activity) + _history.value.filter { it.fileName != _resumeState.value.fileName }
        
        _resumeState.value = _resumeState.value.copy(status = UploadStatus.COMPLETED)
        android.widget.Toast.makeText(context, "AI Analysis Complete!", android.widget.Toast.LENGTH_SHORT).show()
      } catch (e: Exception) {
        _analysisState.value = _analysisState.value.copy(
          isRealSelected = true,
          isAnalysisAvailable = false,
          isError = true
        )
        _resumeState.value = _resumeState.value.copy(status = UploadStatus.COMPLETED) // keep uploaded
        android.widget.Toast.makeText(context, "Gemini Analysis Error: ${e.localizedMessage}", android.widget.Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun extractNameHelper(text: String): String {
    // Attempt to parse first 2 words if they hold capitalized tokens or letters
    val words = text.split(" ").filter { it.isNotBlank() && it.firstOrNull()?.isUpperCase() == true }
    if (words.size >= 2) {
      val candidate = "${words[0].trim()} ${words[1].trim()}"
      // Sanitize candidates
      val filtered = candidate.filter { it.isLetter() || it.isWhitespace() }
      if (filtered.length in 5..30) return filtered
    }
    return ""
  }


  fun resetUpload() {
    _resumeState.value = ResumeState()
  }

  fun startLearning(skillName: String) {
    _missingSkills.value = _missingSkills.value.map {
      if (it.name == skillName) it.copy(isLearning = true) else it
    }

    _roadmap.value = _roadmap.value.map { milestone ->
      if (milestone.title.contains(skillName, ignoreCase = true) || skillName.lowercase().contains(milestone.title.split(" ").first().lowercase())) {
        milestone.copy(status = MilestoneStatus.IN_PROGRESS)
      } else {
        milestone
      }
    }
  }

  fun completeMilestone(week: Int) {
    _roadmap.value = _roadmap.value.map { milestone ->
      if (milestone.week == week) {
        milestone.copy(status = MilestoneStatus.COMPLETED)
      } else if (milestone.week == week + 1 && milestone.status == MilestoneStatus.LOCKED) {
        milestone.copy(status = MilestoneStatus.IN_PROGRESS)
      } else {
        milestone
      }
    }

    // Unlock badges dynamically
    _badges.value = _badges.value.map { badge ->
      if (badge.title == "Milestone Master" && week >= 2) {
        badge.copy(isLocked = false, dateEarned = "Earned on Week $week")
      } else if (badge.title == "Cloud Creator" && week >= 4) {
        badge.copy(isLocked = false, dateEarned = "Earned on Week $week")
      } else {
        badge
      }
    }
  }
}
