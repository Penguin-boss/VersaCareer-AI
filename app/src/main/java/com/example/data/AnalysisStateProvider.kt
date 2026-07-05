package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AnalysisStateProvider {

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

  fun updateResumeState(newState: ResumeState) {
    _resumeState.value = newState
  }

  fun updateAnalysisState(newState: ResumeAnalysisState) {
    _analysisState.value = newState
  }

  fun updateCurrentSkills(newSkills: List<SkillItem>) {
    _currentSkills.value = newSkills
  }

  fun updateMissingSkills(newSkills: List<MissingSkillItem>) {
    _missingSkills.value = newSkills
  }

  fun updateRoadmap(newRoadmap: List<Milestone>) {
    _roadmap.value = newRoadmap
  }

  fun resetAll() {
    _resumeState.value = ResumeState()
    _analysisState.value = ResumeAnalysisState()
    _currentSkills.value = emptyList()
    _missingSkills.value = emptyList()
    _roadmap.value = emptyList()
  }
}
