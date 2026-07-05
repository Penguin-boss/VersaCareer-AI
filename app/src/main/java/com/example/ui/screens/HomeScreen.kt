package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ScoreLineTrendChart
import com.example.ui.components.VersaCareerLogo
import com.example.viewmodel.CareerViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: CareerViewModel,
  onNavigateToUpload: () -> Unit,
  onNavigateToDashboard: () -> Unit,
  onNavigateToAnalysis: () -> Unit,
  onNavigateToSkillGap: () -> Unit,
  onNavigateToRoadmap: () -> Unit,
  onNavigateToJobReadiness: () -> Unit,
  onNavigateToFutureEmployability: () -> Unit,
  onNavigateToProjectRecommendation: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val topScore by viewModel.topScore.collectAsState()
  val missingSkills by viewModel.missingSkills.collectAsState()
  val roadmap by viewModel.roadmap.collectAsState()
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BrandBackground)
  ) {
    // Subtle animated backglow background
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(
          brush = Brush.radialGradient(
            colors = listOf(BrandPrimary.copy(alpha = 0.15f), Color.Transparent),
            radius = 600f
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 24.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      // Top spacing for header bar safety
      Spacer(modifier = Modifier.height(8.dp))

      // Official Logo & Branding Header
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        VersaCareerLogo(
          modifier = Modifier.size(56.dp)
        )
        Column {
          Text(
            text = "VersaCareer AI",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = (-0.5).sp
            ),
            color = Color.White
          )
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = BrandSecondary,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Your AI Career Copilot",
              style = MaterialTheme.typography.labelSmall,
              color = BrandSecondary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Hero Headline
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "Analyze.\nUpskill.\nSucceed.",
          style = MaterialTheme.typography.displayLarge.copy(
            lineHeight = 44.sp,
            fontWeight = FontWeight.Black
          ),
          color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "AI-powered career intelligence that identifies skill gaps, predicts employability, and creates personalized growth roadmaps.",
          style = MaterialTheme.typography.bodyLarge,
          color = BrandSecondaryText,
          lineHeight = 22.sp
        )
      }

      // Call to Action Buttons
      Button(
        onClick = onNavigateToUpload,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("home_upload_resume_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            "Upload Resume",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.Default.TrendingFlat,
            contentDescription = "Arrow Right",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      // Your Intelligence Hub Section
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            "Your Intelligence Hub",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            if (analysis.isAnalysisAvailable) "Updated just now" else "Awaiting analysis",
            style = MaterialTheme.typography.labelSmall,
            color = BrandSecondary
          )
        }

        // Dashboard Hub Card
        ElevatedCard(
          onClick = onNavigateToDashboard,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("intelligence_hub_card"),
          colors = CardDefaults.elevatedCardColors(containerColor = BrandCardBg),
          shape = RoundedCornerShape(24.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "Resume Score",
                  style = MaterialTheme.typography.labelMedium,
                  color = BrandSecondaryText
                )
                Row(
                  verticalAlignment = Alignment.Bottom,
                  modifier = Modifier.padding(vertical = 4.dp)
                ) {
                  Text(
                    text = if (analysis.isAnalysisAvailable) "$topScore" else "0",
                    style = TextStyle(
                      fontSize = 44.sp,
                      fontWeight = FontWeight.Black,
                      fontFamily = BrandFontFamily
                    ),
                    color = Color.White
                  )
                  Text(
                    "/100",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandSecondaryText,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                }
                Text(
                  text = if (analysis.isAnalysisAvailable) "Top 12% of users" else "Upload and analyze a resume to generate results.",
                  style = MaterialTheme.typography.labelMedium,
                  color = if (analysis.isAnalysisAvailable) BrandPrimary else BrandSecondaryText,
                  fontWeight = FontWeight.SemiBold
                )
                if (analysis.isAnalysisAvailable) {
                  Text(
                    "+8 improvement this month",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentGreen,
                    fontWeight = FontWeight.Normal
                  )
                }
              }

              // Circular score chart
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(90.dp)
              ) {
                CircularProgressIndicator(
                  progress = { if (analysis.isAnalysisAvailable) topScore / 100f else 0f },
                  modifier = Modifier.fillMaxSize(),
                  color = BrandSecondary,
                  strokeWidth = 8.dp,
                  trackColor = Color(0xFF334155)
                )
                Text(
                  text = if (analysis.isAnalysisAvailable) "$topScore%" else "0%",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            // Trend Score Chart
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF0F172A).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(8.dp)
            ) {
              ScoreLineTrendChart(
                scores = if (analysis.isAnalysisAvailable) listOf(68f, 72f, 74f, 80f, topScore.toFloat()) else listOf(0f),
                modifier = Modifier.fillMaxSize(),
                lineColor = BrandSecondary
              )
            }
          }
        }
      }

      // Feature cards grid lists
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          "Core Intelligence Suites",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )

        // Single elegant banner above the feature grid when resume is not analysed yet (as required by section 3)
        if (!analysis.isAnalysisAvailable) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A).copy(alpha = 0.35f)),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF2563EB).copy(alpha = 0.3f))
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF60A5FA),
                modifier = Modifier.size(20.dp)
              )
              Text(
                text = "Upload a resume to unlock all career intelligence modules.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF93C5FD),
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FeatureCard(
            title = "Resume Analysis",
            description = "ATS Compatibility",
            icon = Icons.Default.Description,
            badge = if (analysis.isAnalysisAvailable) "Score $topScore" else "",
            modifier = Modifier.weight(1f),
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToAnalysis
          )
          FeatureCard(
            title = "Skill Gap Analysis",
            description = "Missing stack check",
            icon = Icons.Default.AutoAwesomeMotion,
            badge = if (analysis.isAnalysisAvailable) "${missingSkills.size} Gaps" else "",
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToSkillGap,
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FeatureCard(
            title = "Job Readiness",
            description = "Target role alignment",
            icon = Icons.Default.BusinessCenter,
            badge = if (analysis.isAnalysisAvailable) "${analysis.technicalSkillsScore}% Ready" else "",
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToJobReadiness,
            modifier = Modifier.weight(1f)
          )
          FeatureCard(
            title = "Future Employability",
            description = "Market resilience scan",
            icon = Icons.Default.Assessment,
            badge = if (analysis.isAnalysisAvailable) "Active Check" else "",
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToFutureEmployability,
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FeatureCard(
            title = "Learning Roadmap",
            description = "Interactive 4-week path",
            icon = Icons.Default.Map,
            badge = if (analysis.isAnalysisAvailable) "${(roadmap.count { it.status == com.example.data.MilestoneStatus.COMPLETED }.toFloat() / roadmap.size.coerceAtLeast(1) * 100).toInt()}% Done" else "",
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToRoadmap,
            modifier = Modifier.weight(1f)
          )
          FeatureCard(
            title = "Project Suggestions",
            description = "Build credentials",
            icon = Icons.Default.Build,
            badge = if (analysis.isAnalysisAvailable) "Verified suggestions" else "",
            isActive = analysis.isAnalysisAvailable,
            onClick = onNavigateToProjectRecommendation,
            modifier = Modifier.weight(1f)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun FeatureCard(
  title: String,
  description: String,
  icon: ImageVector,
  badge: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isActive: Boolean = true
) {
  Card(
    modifier = modifier
      .height(116.dp) // Reduced excessive height by ~17% to be compact and balanced
      .clickable(onClick = onClick)
      .then(if (!isActive) Modifier.alpha(0.55f) else Modifier),
    colors = CardDefaults.cardColors(containerColor = BrandCardBg),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, Color(0xFF334155))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .background(BrandPrimary.copy(alpha = 0.15f), CircleShape)
            .border(1.dp, BrandPrimary.copy(alpha = 0.25f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(16.dp)
          )
        }
        
        if (isActive && badge.isNotEmpty()) {
          Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall,
            color = BrandSecondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .background(Color(0xFF334155).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 15.sp
          ),
          color = Color.White,
          maxLines = 2
        )
        Text(
          text = description,
          style = MaterialTheme.typography.labelSmall,
          color = BrandSecondaryText,
          fontSize = 10.sp,
          maxLines = 1,
          modifier = Modifier.padding(top = 1.dp)
        )
      }
    }
  }
}
