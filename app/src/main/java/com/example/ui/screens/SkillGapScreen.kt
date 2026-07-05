package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import com.example.data.*
import com.example.ui.components.RadarData
import com.example.ui.components.SkillRadarChart
import com.example.ui.components.SubScreenAppBar
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel

@Composable
fun SkillGapScreen(
  viewModel: CareerViewModel,
  onBackClick: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val currentSkills by viewModel.currentSkills.collectAsState()
  val missingSkills by viewModel.missingSkills.collectAsState()
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      SubScreenAppBar(
        title = "Skill Gap Analysis",
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
                text = "Analyze a resume to view skill gaps.",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )
              Text(
                text = "Once the AI analysis engine is connected, your complete technical competency checklist and detected skill gaps will be detailed right here.",
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
        // Header Title
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Skill Gap Analysis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Visualize your current skill stack against market demands.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        // Competency Map Card (Radial Map)
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
            Text(
              text = "Competency Map",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))

            SkillRadarChart(
              axes = listOf(
                RadarData("Backend", (analysis.technicalSkillsScore / 100f).coerceIn(0.1f, 1.0f)),
                RadarData("Frontend", (analysis.technicalSkillsScore / 100f * 0.9f).coerceIn(0.1f, 1.0f)),
                RadarData("DevOps", (analysis.marketRelevanceScore / 100f).coerceIn(0.1f, 1.0f)),
                RadarData("Databases", (analysis.projectQualityScore / 100f).coerceIn(0.1f, 1.0f)),
                RadarData("Cloud", (analysis.futureEmployabilityScore / 100f).coerceIn(0.1f, 1.0f)),
                RadarData("Tools", (analysis.atsCompatibilityScore / 100f).coerceIn(0.1f, 1.0f))
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
              primaryColor = BrandPrimary,
              secondaryColor = BrandSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chart Legends
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .background(BrandPrimary, CircleShape)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Your Skills", style = MaterialTheme.typography.labelSmall, color = Color.White)

              Spacer(modifier = Modifier.width(20.dp))

              Box(
                modifier = Modifier
                  .size(10.dp)
                  .background(Color(0xFF334155), CircleShape)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Required Limits", style = MaterialTheme.typography.labelSmall, color = BrandSecondaryText)
            }
          }
        }

        // Current Stack Section
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "CURRENT STACK",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.1.sp
          )

          // Flow-like custom grid for skills chips
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            currentSkills.chunked(2).forEach { chunk ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                chunk.forEach { skill ->
                  CardSkillChip(skill, Modifier.weight(1f))
                }
                if (chunk.size == 1) {
                  Box(modifier = Modifier.weight(1f))
                }
              }
            }
          }
        }

        // Missing Skills List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "MISSING SKILLS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.1.sp
          )

          missingSkills.forEach { skill ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = BrandCardBg),
              shape = RoundedCornerShape(16.dp),
              border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  modifier = Modifier.weight(1f),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(40.dp)
                      .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Book,
                      contentDescription = null,
                      tint = BrandSecondary,
                      modifier = Modifier.size(18.dp)
                    )
                  }

                  Column {
                    Text(
                      skill.name,
                      style = MaterialTheme.typography.bodyLarge,
                      color = Color.White,
                      fontWeight = FontWeight.Bold
                    )
                    val badgeColor = when (skill.priority) {
                      Priority.HIGH -> AccentRed.copy(alpha = 0.15f)
                      Priority.MEDIUM -> AccentOrange.copy(alpha = 0.15f)
                      Priority.LOW -> AccentGreen.copy(alpha = 0.15f)
                    }
                    val badgeLabel = when (skill.priority) {
                      Priority.HIGH -> "HIGH PRIORITY"
                      Priority.MEDIUM -> "MEDIUM PRIORITY"
                      Priority.LOW -> "LOW PRIORITY"
                    }
                    val badgeTextColor = when (skill.priority) {
                      Priority.HIGH -> AccentRed
                      Priority.MEDIUM -> AccentOrange
                      Priority.LOW -> AccentGreen
                    }

                    Text(
                      text = "🔥 $badgeLabel",
                      color = badgeTextColor,
                      style = MaterialTheme.typography.labelSmall,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }

                Column(
                  horizontalAlignment = Alignment.End,
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Button(
                    onClick = {
                      viewModel.startLearning(skill.name)
                    },
                    modifier = Modifier
                      .height(32.dp)
                      .testTag("learn_skill_gap_${skill.name}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                      containerColor = if (skill.isLearning) Color(0xFF334155) else BrandPrimary
                    )
                  ) {
                    Text(
                      text = if (skill.isLearning) "Learning" else "Learn",
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  }

                  OutlinedButton(
                    onClick = {
                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(skill.resourcesLink))
                      context.startActivity(intent)
                    },
                    modifier = Modifier
                      .height(30.dp)
                      .testTag("resources_skill_gap_${skill.name}"),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.Center
                    ) {
                      Text(
                        "Resources",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandSecondaryText,
                        fontSize = 10.sp
                      )
                      Spacer(modifier = Modifier.width(2.dp))
                      Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = null,
                        tint = BrandSecondaryText,
                        modifier = Modifier.size(10.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
      } // closes else
    } // closes Box
  } // closes Scaffold
}

@Composable
fun CardSkillChip(skill: SkillItem, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier.height(52.dp),
    colors = CardDefaults.cardColors(containerColor = BrandPrimary.copy(alpha = 0.1f)),
    shape = RoundedCornerShape(12.dp),
    border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.2f))
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = skill.name,
          color = Color.White,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF22D3EE).copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = skill.level,
            color = BrandSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
