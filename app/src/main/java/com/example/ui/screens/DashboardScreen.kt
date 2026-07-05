package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MissingSkillItem
import com.example.data.Priority
import com.example.ui.components.RadarData
import com.example.ui.components.SkillRadarChart
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel

@Composable
fun DashboardScreen(
  viewModel: CareerViewModel,
  onNavigateToAnalysis: () -> Unit,
  onNavigateToSkillGap: () -> Unit,
  onNavigateToRoadmap: () -> Unit,
  onNavigateToJobReadiness: () -> Unit,
  onNavigateToFutureEmployability: () -> Unit,
  onNavigateToProjectRecommendation: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val missingSkills by viewModel.missingSkills.collectAsState()
  val roadmap by viewModel.roadmap.collectAsState()
  val scrollState = rememberScrollState()

  val totalMilestones = roadmap.size
  val completedMilestones = roadmap.count { m -> m.status == com.example.data.MilestoneStatus.COMPLETED }
  val roadmapProgress = if (totalMilestones > 0) completedMilestones.toFloat() / totalMilestones else 0.0f
  val roadmapPercentString = "${(roadmapProgress * 100).toInt()}% complete"

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BrandBackground)
  ) {
    if (!analysis.isAnalysisAvailable) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("not_available_card"),
          colors = CardDefaults.cardColors(containerColor = BrandCardBg),
          shape = RoundedCornerShape(24.dp),
          border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = BrandSecondary,
                modifier = Modifier.size(36.dp)
              )
            }
            Text(
              text = "Analysis not available yet",
              color = Color.White,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center
            )
            Text(
              text = "Please upload a resume under the Upload tab to perform full evaluations and identify skill gaps.",
              color = BrandSecondaryText,
              style = MaterialTheme.typography.bodyMedium,
              textAlign = TextAlign.Center,
              lineHeight = 20.sp
            )
          }
        }
      }
    } else {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
      ) {
      // Top spacing for floating header safety
      Spacer(modifier = Modifier.height(8.dp))

      // Header Welcome
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Intelligence Center",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Evaluation powered by GPT + Gemini models",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        Box(
          modifier = Modifier
            .size(38.dp)
            .background(BrandSecondary.copy(alpha = 0.1f), CircleShape)
            .border(1.dp, BrandSecondary.copy(alpha = 0.2f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.BarChart,
            contentDescription = "Stats",
            tint = BrandSecondary,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Live Resume Score Metric Card
      ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = BrandCardBg)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                "Current Score",
                style = MaterialTheme.typography.labelSmall,
                color = BrandSecondaryText,
                fontWeight = FontWeight.Bold
              )
              Text(
                "${analysis.overallScore}/100",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Black
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF22D3EE).copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                "ATS Optimal",
                color = BrandSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 4 Grid Alignment Bars
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricProgressRow("Technical Skills", analysis.technicalSkillsScore / 100f, "${analysis.technicalSkillsScore}%")
            MetricProgressRow("Projects Execution", analysis.projectQualityScore / 100f, "${analysis.projectQualityScore}%")
            MetricProgressRow("ATS Compatibility", analysis.atsCompatibilityScore / 100f, "${analysis.atsCompatibilityScore}%")
            MetricProgressRow("Market Relevance", analysis.marketRelevanceScore / 100f, "${analysis.marketRelevanceScore}%")
          }
        }
      }

      // Side-by-side: Job Readiness & Future Employability Cards
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Job Readiness Card
        Card(
          onClick = onNavigateToJobReadiness,
          modifier = Modifier
            .weight(1f)
            .height(180.dp)
            .testTag("dashboard_job_readiness_card"),
          colors = CardDefaults.cardColors(containerColor = BrandCardBg),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = BrandSecondary,
                modifier = Modifier.size(20.dp)
              )
              Text(
                "Job Readiness",
                style = MaterialTheme.typography.labelSmall,
                color = BrandSecondaryText,
                fontWeight = FontWeight.Bold
              )
            }

            Text(
              "${analysis.jobReadinessScore}%",
              style = MaterialTheme.typography.displayMedium,
              color = Color.White,
              fontWeight = FontWeight.Black
            )

            LinearProgressIndicator(
              progress = { analysis.jobReadinessScore / 100f },
              color = BrandSecondary,
              trackColor = Color(0xFF0F172A),
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
              Text(
                "✔ Frontend Dev Focus",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 10.sp
              )
              Text(
                "✔ Junior Soft Eng",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 10.sp
              )
            }
          }
        }

        // Future Employability Card
        Card(
          onClick = onNavigateToFutureEmployability,
          modifier = Modifier
            .weight(1f)
            .height(180.dp)
            .testTag("dashboard_future_employability_card"),
          colors = CardDefaults.cardColors(containerColor = BrandCardBg),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.TrackChanges,
                contentDescription = null,
                tint = BrandPrimary,
                modifier = Modifier.size(20.dp)
              )
              Text(
                "Market Index",
                style = MaterialTheme.typography.labelSmall,
                color = BrandSecondaryText,
                fontWeight = FontWeight.Bold
              )
            }

            Text(
              "${analysis.futureEmployabilityScore}%",
              style = MaterialTheme.typography.displayMedium,
              color = Color.White,
              fontWeight = FontWeight.Black
            )

            Column {
              Text(
                "Growth: +24%",
                color = AccentGreen,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
              Text(
                "Risk Level: Low",
                color = BrandSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }

            Text(
              "High future resilience",
              style = MaterialTheme.typography.labelSmall,
              color = BrandSecondaryText,
              fontSize = 9.sp
            )
          }
        }
      }

      // Career Skill Radar Chart Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrandCardBg),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              "Career Skill Radar",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Icon(
              imageVector = Icons.Default.CompassCalibration,
              contentDescription = null,
              tint = BrandSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          SkillRadarChart(
            axes = listOf(
              RadarData("Programming", (analysis.technicalSkillsScore / 100f).coerceIn(0.1f, 1.0f)),
              RadarData("Projects", (analysis.projectQualityScore / 100f).coerceIn(0.1f, 1.0f)),
              RadarData("Optimization", (analysis.atsCompatibilityScore / 100f).coerceIn(0.1f, 1.0f)),
              RadarData("Market Link", (analysis.marketRelevanceScore / 100f).coerceIn(0.1f, 1.0f)),
              RadarData("Cloud Setup", (analysis.futureEmployabilityScore / 100f).coerceIn(0.1f, 1.0f))
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(220.dp)
          )
        }
      }

      // Missing Skills Section Checklist
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            "Missing Skills Gap",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            "View map",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondary,
            modifier = Modifier.clickable { onNavigateToSkillGap() }
          )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          missingSkills.take(4).forEach { item ->
            MissingSkillRow(item) {
              viewModel.startLearning(item.name)
            }
          }
        }
      }

      // Career Growth Timeline Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrandCardBg),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                "Career Growth Timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                "Predicted roadmap milestones",
                style = MaterialTheme.typography.labelSmall,
                color = BrandSecondaryText
              )
            }
            Text(
              roadmapPercentString,
              color = BrandSecondary,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          LinearProgressIndicator(
            progress = { roadmapProgress },
            color = BrandSecondary,
            trackColor = Color(0xFF0F172A),
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(CircleShape)
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Vertical timeline nodes list
          val currentScore = analysis.overallScore
          val m3Score = (currentScore + 6).coerceAtMost(100)
          val m6Score = (currentScore + 12).coerceAtMost(100)
          val m12Score = (currentScore + 18).coerceAtMost(100)

          TimelineNodeItem("Current Score", "$currentScore", "ATS-ready foundation", true, true)
          TimelineNodeItem("3 Months Model", "$m3Score", "Improve focus stack coverage", true, false)
          TimelineNodeItem("6 Months Model", "$m6Score", "Certify credentials and cloud path", false, false)
          TimelineNodeItem("1 Year Model", "$m12Score", "Senior-track growth goals", false, false, isLastNode = true)
        }
      }

      // Explore Features Quick Panel
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          "Explore Features",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          QuickFeatureTile("Resume Analysis", Icons.Default.Description, onNavigateToAnalysis, Modifier.weight(1f))
          QuickFeatureTile("Skill Gap Analysis", Icons.Default.CenterFocusStrong, onNavigateToSkillGap, Modifier.weight(1f))
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          QuickFeatureTile("Job Readiness", Icons.Default.Handshake, onNavigateToJobReadiness, Modifier.weight(1f))
          QuickFeatureTile("Project Suggestions", Icons.Default.Folder, onNavigateToProjectRecommendation, Modifier.weight(1f))
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          QuickFeatureTile("Learning Roadmap", Icons.Default.Map, onNavigateToRoadmap, Modifier.weight(1f))
          QuickFeatureTile("Opportunity Finder", Icons.Default.Search, {}, Modifier.weight(1f))
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
    } // Closes else
  } // Closes Box
}

@Composable
fun MetricProgressRow(title: String, percentage: Float, value: String) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        color = BrandSecondaryText
      )
      Text(
        text = value,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    }
    LinearProgressIndicator(
      progress = { percentage },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(CircleShape),
      color = BrandPrimary,
      trackColor = Color(0xFF0F172A)
    )
  }
}

@Composable
fun MissingSkillRow(item: MissingSkillItem, onLearnClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF1E293B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
      .border(1.dp, Color(0xFF334155).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
      .padding(horizontal = 14.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = item.name,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Spacer(modifier = Modifier.width(8.dp))
      val badgeColor = when (item.priority) {
        Priority.HIGH -> AccentRed.copy(alpha = 0.15f)
        Priority.MEDIUM -> AccentOrange.copy(alpha = 0.15f)
        Priority.LOW -> AccentGreen.copy(alpha = 0.15f)
      }
      val badgeText = when (item.priority) {
        Priority.HIGH -> "High"
        Priority.MEDIUM -> "Medium"
        Priority.LOW -> "Low"
      }
      val badgeTextColor = when (item.priority) {
        Priority.HIGH -> AccentRed
        Priority.MEDIUM -> AccentOrange
        Priority.LOW -> AccentGreen
      }


      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(badgeColor)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          badgeText,
          color = badgeTextColor,
          style = MaterialTheme.typography.labelSmall,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Button(
      onClick = onLearnClick,
      modifier = Modifier
        .height(32.dp)
        .testTag("learn_skill_${item.name}"),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (item.isLearning) Color(0xFF334155) else Color(0xFF0F172A)
      ),
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
      Text(
        text = if (item.isLearning) "In Progress" else "Learn",
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun TimelineNodeItem(
  title: String,
  score: String,
  desc: String,
  isAchieved: Boolean,
  isActive: Boolean,
  isLastNode: Boolean = false
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(24.dp)
    ) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .background(
            if (isAchieved) BrandSecondary else Color(0xFF334155),
            CircleShape
          )
          .border(
            width = if (isActive) 4.dp else 1.dp,
            color = if (isActive) Color.White else Color.Transparent,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isAchieved) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF0F172A),
            modifier = Modifier.size(10.dp)
          )
        }
      }

      if (!isLastNode) {
        Box(
          modifier = Modifier
            .width(2.dp)
            .height(50.dp)
            .background(if (isAchieved) BrandSecondary else Color(0xFF334155))
        )
      }
    }

    Row(
      modifier = Modifier
        .weight(1f)
        .padding(bottom = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          color = if (isActive || isAchieved) Color.White else BrandSecondaryText
        )
        Text(
          desc,
          style = MaterialTheme.typography.labelSmall,
          color = BrandSecondaryText
        )
      }

      Text(
        score,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = if (isAchieved) BrandSecondary else BrandSecondaryText
      )
    }
  }
}

@Composable
fun QuickFeatureTile(
  title: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .height(64.dp)
      .clickable(onClick = onClick),
    colors = CardDefaults.cardColors(containerColor = BrandCardBg),
    shape = RoundedCornerShape(12.dp),
    border = BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = BrandSecondary,
          modifier = Modifier.size(16.dp)
        )
      }
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        lineHeight = 14.sp
      )
    }
  }
}
