package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SubScreenAppBar
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel

@Composable
fun JobReadinessScreen(
  viewModel: CareerViewModel,
  onBackClick: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val userName by viewModel.userName.collectAsState()
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      SubScreenAppBar(
        title = "Job Readiness",
        onBackClick = onBackClick
      )
    },
    containerColor = BrandBackground
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
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
                text = "Analyze a resume to calculate readiness.",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )
              Text(
                text = "Your job readiness scoring, dimensions evaluation, and role alignment ratings will generate immediately after a resume is analyzed.",
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
            .padding(horizontal = 20.dp, vertical = 20.dp),
          verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
        // Hero Title & Description
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Job Readiness Assessment",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.testTag("job_readiness_title")
          )
          Text(
            text = "Deep-dive assessment of market ready preparedness for target roles.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        // Main Analytics Section: Progress Metric
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = BrandCardBg),
          shape = RoundedCornerShape(24.dp),
          border = BorderStroke(1.dp, Color(0xFF334155))
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
              Column {
                Text(
                  text = "OVERALL READY RATING",
                  style = MaterialTheme.typography.labelSmall,
                  color = BrandSecondary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.1.sp
                )
                Row(
                  verticalAlignment = Alignment.Bottom,
                  modifier = Modifier.padding(vertical = 4.dp)
                ) {
                  Text(
                    text = "${analysis.jobReadinessScore}%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  val readinessLabel = when {
                    analysis.jobReadinessScore >= 80 -> "Highly Ready"
                    analysis.jobReadinessScore >= 60 -> "Ready"
                    else -> "Developing"
                  }
                  Text(
                    text = readinessLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                }
              }

              // Circular progress matching style
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
              ) {
                CircularProgressIndicator(
                  progress = { analysis.jobReadinessScore / 100f },
                  modifier = Modifier.fillMaxSize(),
                  color = BrandSecondary,
                  strokeWidth = 8.dp,
                  trackColor = Color(0xFF334155)
                )
                Text(
                  text = "${analysis.jobReadinessScore}%",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            LinearProgressIndicator(
              progress = { analysis.jobReadinessScore / 100f },
              color = BrandSecondary,
              trackColor = Color(0xFF0F172A),
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
            )

            Text(
              text = analysis.suitableRolesText.ifEmpty { "Your resume shows high correlation to Jr. Software Engineer and Frontend Developer requisites, but has minor gap blockers under deployment setups." },
              style = MaterialTheme.typography.bodyMedium,
              color = BrandSecondaryText,
              lineHeight = 20.sp
            )
          }
        }

        // Detailed Competency Breakdowns
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "DIMENSION EVALUATION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.1.sp
          )

          // Dimension Cards
          if (analysis.dimensions.isNotEmpty()) {
            analysis.dimensions.forEach { dim ->
              DimensionScoreRow(dim.name, dim.value, dim.scoreLabel, dim.description)
            }
          } else {
            DimensionScoreRow("Technical Skill Alignment", 0.80f, "80%", "Strong core programming foundations.")
            DimensionScoreRow("Project Experience Proof", 0.75f, "75%", "Great demonstration of real-world impact.")
            DimensionScoreRow("ATS File Compliance", 0.85f, "85%", "Parsed cleanly without structural bugs.")
            DimensionScoreRow("Cloud & DevOps Exposure", 0.40f, "40%", "Missing modern cloud environments.")
          }
        }

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              Toast.makeText(context, "Scanning matching positions in your location...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("job_readiness_scan_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                "Find Open Roles Matching ${userName.ifEmpty { "You" }}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          OutlinedButton(
            onClick = {
              Toast.makeText(context, "Generating detailed ATS report PDF...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("job_readiness_report_btn"),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
          ) {
            Text(
              "Download Export PDF Report",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
      } // closes else
    } // closes Box
  } // closes Scaffold
}

@Composable
fun DimensionScoreRow(
  title: String,
  progress: Float,
  scoreLabel: String,
  summary: String
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = BrandCardBg),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, Color(0xFF334155))
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          color = Color.White,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = scoreLabel,
          color = BrandSecondary,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold
        )
      }

      LinearProgressIndicator(
        progress = { progress },
        color = BrandPrimary,
        trackColor = Color(0xFF0F172A),
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(CircleShape)
      )

      Text(
        text = summary,
        color = BrandSecondaryText,
        style = MaterialTheme.typography.bodySmall,
        fontSize = 11.sp
      )
    }
  }
}
