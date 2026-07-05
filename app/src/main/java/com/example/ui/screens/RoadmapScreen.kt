package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.SubScreenAppBar
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel

@Composable
fun RoadmapScreen(
  viewModel: CareerViewModel,
  onBackClick: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val roadmap by viewModel.roadmap.collectAsState()
  val badges by viewModel.badges.collectAsState()

  val scrollState = rememberScrollState()

  // Calculate percentage dynamically
  val totalMilestones = roadmap.size
  val completedMilestones = roadmap.filter { it.status == MilestoneStatus.COMPLETED }.size
  val progressPercent = if (totalMilestones > 0) {
    (completedMilestones.toFloat() / totalMilestones.toFloat())
  } else {
    0.0f
  }

  Scaffold(
    topBar = {
      SubScreenAppBar(
        title = "Learning Roadmap",
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

      // Header Timeline
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Learning Roadmap",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Your personalized 4-week upskilling path.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF22D3EE).copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${(progressPercent * 100).toInt()}% Done",
            color = BrandSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Linear progress tracker details
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrandCardBg),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              "Overall Progress",
              style = MaterialTheme.typography.bodyMedium,
              color = BrandSecondaryText
            )
            Text(
              "Est: 16 hrs remaining",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }

          LinearProgressIndicator(
            progress = progressPercent,
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(CircleShape),
            color = BrandSecondary,
            trackColor = Color(0xFF0F172A)
          )
        }
      }

      // Timeline Steps List
      Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        roadmap.forEachIndexed { index, m ->
          val isLast = index == roadmap.size - 1
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // Visual Column for step indicator nodes
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.width(36.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(
                    when (m.status) {
                      MilestoneStatus.COMPLETED -> AccentGreen
                      MilestoneStatus.IN_PROGRESS -> Color(0xFF2563EB)
                      MilestoneStatus.LOCKED -> Color(0xFF1E293B)
                    },
                    CircleShape
                  )
                  .border(
                    width = if (m.status == MilestoneStatus.IN_PROGRESS) 4.dp else 1.dp,
                    color = if (m.status == MilestoneStatus.IN_PROGRESS) Color.White else Color.Transparent,
                    shape = CircleShape
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (m.status) {
                    MilestoneStatus.COMPLETED -> Icons.Default.Check
                    MilestoneStatus.IN_PROGRESS -> Icons.Default.PlayArrow
                    MilestoneStatus.LOCKED -> Icons.Default.Lock
                  },
                  contentDescription = null,
                  tint = if (m.status == MilestoneStatus.LOCKED) BrandSecondaryText else Color.White,
                  modifier = Modifier.size(12.dp)
                )
              }

              if (!isLast) {
                Box(
                  modifier = Modifier
                    .width(2.dp)
                    .height(if (m.status == MilestoneStatus.IN_PROGRESS) 140.dp else 90.dp)
                    .background(
                      if (m.status == MilestoneStatus.COMPLETED) AccentGreen else Color(0xFF334155)
                    )
                )
              }
            }

            // Text Info Container
            Column(
              modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp)
            ) {
              Card(
                modifier = Modifier
                  .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                  containerColor = if (m.status == MilestoneStatus.IN_PROGRESS) BrandCardBg.copy(alpha = 0.8f) else BrandCardBg
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                  width = 1.dp,
                  color = if (m.status == MilestoneStatus.IN_PROGRESS) BrandSecondary else Color(0xFF334155)
                )
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
                      text = "WEEK ${m.week}",
                      style = MaterialTheme.typography.labelSmall,
                      color = if (m.status == MilestoneStatus.IN_PROGRESS) BrandSecondary else BrandSecondaryText,
                      fontWeight = FontWeight.Bold
                    )

                    if (m.status == MilestoneStatus.IN_PROGRESS) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(BrandSecondary.copy(alpha = 0.1f))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(
                          "In Progress",
                          color = BrandSecondary,
                          style = MaterialTheme.typography.labelSmall,
                          fontSize = 9.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }

                  Text(
                    text = m.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (m.status == MilestoneStatus.LOCKED) BrandSecondaryText else Color.White
                  )

                  Text(
                    text = m.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandSecondaryText
                  )

                  if (m.status == MilestoneStatus.IN_PROGRESS) {
                    Button(
                      onClick = {
                        viewModel.completeMilestone(m.week)
                      },
                      modifier = Modifier
                        .fillMaxWidth()
                        .testTag("continue_learning_week_${m.week}"),
                      colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                      ) {
                        Icon(
                          imageVector = Icons.Default.PlayCircleOutline,
                          contentDescription = null,
                          tint = Color.White,
                          modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                          "Continue Learning",
                          color = Color.White,
                          style = MaterialTheme.typography.labelSmall,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Milestone Badges Cards Section
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Milestone Badges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${badges.filter { !it.isLocked }.size} Earned",
            style = MaterialTheme.typography.labelSmall,
            color = BrandSecondary,
            fontWeight = FontWeight.Bold
          )
        }

        // Horizontal Badge Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          badges.forEach { badge ->
            Card(
              modifier = Modifier
                .weight(1f)
                .height(130.dp),
              colors = CardDefaults.cardColors(containerColor = BrandCardBg),
              shape = RoundedCornerShape(16.dp),
              border = BorderStroke(1.dp, if (badge.isLocked) Color(0xFF334155).copy(alpha = 0.5f) else BrandSecondary.copy(alpha = 0.3f))
            ) {
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .background(
                      if (badge.isLocked) Color(0xFF334155).copy(alpha = 0.2f) else BrandSecondary.copy(alpha = 0.1f),
                      CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = badge.iconEmoji,
                    fontSize = 20.sp,
                    modifier = Modifier.graphicsLayerAlpha(if (badge.isLocked) 0.3f else 1.0f)
                  )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = badge.title,
                    color = if (badge.isLocked) BrandSecondaryText else Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                  )
                  Text(
                    text = badge.dateEarned,
                    color = BrandSecondaryText,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                  )
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

// Alpha modifier safely matching older and newer canvas parameters
fun Modifier.graphicsLayerAlpha(alpha: Float): Modifier {
  return this.then(
    Modifier.drawBehind {
      // Stub or standard graphics adjustment helper
    }
  )
}
