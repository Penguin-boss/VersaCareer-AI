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
fun ProjectRecommendationScreen(
  viewModel: CareerViewModel,
  onNavigateToRoadmap: () -> Unit,
  onBackClick: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      SubScreenAppBar(
        title = "Project Suggestions",
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
                text = "Analyze a resume to generate project recommendations.",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )
              Text(
                text = "Targeted project suggestions, technical blueprints, and portfolio strength tracking will generate once your resume analysis is complete.",
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
        // Hero Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Project Suggestions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.testTag("project_recommendation_title")
          )
          Text(
            text = "Hands-on projects strategically curated to bridge identified skill gaps (AWS, Docker, SQL) and enrich your portfolio with ATS-optimal metrics.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        // Main Analytics Section: Core Highlights Card
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
                  text = "PORTFOLIO STRENGTH",
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
                    text = "${analysis.portfolioStrengthScore}%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  val strengthLabel = when {
                    analysis.portfolioStrengthScore >= 80 -> "Advanced"
                    analysis.portfolioStrengthScore >= 60 -> "Intermediate"
                    else -> "Developing"
                  }
                  Text(
                    text = strengthLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                }
              }

              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
              ) {
                CircularProgressIndicator(
                  progress = { analysis.portfolioStrengthScore / 100f },
                  modifier = Modifier.fillMaxSize(),
                  color = BrandSecondary,
                  strokeWidth = 8.dp,
                  trackColor = Color(0xFF334155)
                )
                Text(
                  text = "${analysis.portfolioStrengthScore}%",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            val projectCount = analysis.recommendedProjects.size
            val dynamicDesc = if (projectCount > 0) {
              "Industry targets high-impact project codebases with microservices. Building the suggested $projectCount curated project blueprints will boost your active portfolio strength by +18%."
            } else {
              "Industry targets high-impact project codebases with microservices. Building 1 AWS containerized infrastructure will boost your resume compatibility by +18%."
            }
            Text(
              text = dynamicDesc,
              style = MaterialTheme.typography.bodyMedium,
              color = BrandSecondaryText,
              lineHeight = 20.sp
            )
          }
        }

        // Recommended Project Items
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
          Text(
            text = "RECOMMENDED ALIGNMENTS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.1.sp
          )

          if (analysis.recommendedProjects.isNotEmpty()) {
            analysis.recommendedProjects.forEach { proj ->
              ProjectBlueprintCard(
                title = proj.title,
                description = proj.description,
                skillsList = proj.skills,
                timeLabel = proj.duration,
                difficulty = proj.difficulty,
                onStartClick = {
                  Toast.makeText(context, "Initializing project: ${proj.title}...", Toast.LENGTH_SHORT).show()
                }
              )
            }
          } else {
            ProjectBlueprintCard(
              title = "AWS Containerized Microservice",
              description = "Build a Spring Boot or Node.js web server containerized with Docker, published inside Amazon Elastic Container Service (ECS) complete with continuous integration.",
              skillsList = listOf("AWS", "Docker", "CI/CD"),
              timeLabel = "2-3 weeks",
              difficulty = "Medium",
              onStartClick = {
                Toast.makeText(context, "Initializing AWS microservice sandbox plan...", Toast.LENGTH_SHORT).show()
              }
            )

            ProjectBlueprintCard(
              title = "High-Volume SQL Analytics Layer",
              description = "Configure a PostgreSQL dashboard backend using window analytical aggregations, materialized query indices, and transaction locks to mimic realistic metrics scales.",
              skillsList = listOf("SQL", "PostgreSQL", "Query Stats"),
              timeLabel = "1 week",
              difficulty = "Easy",
              onStartClick = {
                Toast.makeText(context, "Loading complex analytical SQL boilerplates...", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }

        // Main Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              Toast.makeText(context, "Scanning Git profiles to match projects...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("project_recs_github_verify_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                "Sync Existing GitHub Projects",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          OutlinedButton(
            onClick = onNavigateToRoadmap,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("project_recs_learning_roadmap_btn"),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
          ) {
            Text(
              "Integrate with Active Roadmap",
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
fun ProjectBlueprintCard(
  title: String,
  description: String,
  skillsList: List<String>,
  timeLabel: String,
  difficulty: String,
  onStartClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = BrandCardBg),
    shape = RoundedCornerShape(20.dp),
    border = BorderStroke(1.dp, Color(0xFF334155))
  ) {
    Column(
      modifier = Modifier.padding(18.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Estimated: $timeLabel • Complexity: $difficulty",
            color = BrandSecondaryText,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BrandPrimary.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "HIGH IMPACT",
            color = BrandSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp
          )
        }
      }

      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = BrandSecondaryText,
        lineHeight = 20.sp
      )

      // Skills chips row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        skillsList.forEach { skill ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF334155).copy(alpha = 0.4f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "⚡ $skill",
              color = Color.White,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 10.sp
            )
          }
        }
      }

      Button(
        onClick = onStartClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary.copy(alpha = 0.8f))
      ) {
        Text("Get Blueprint Details", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
      }
    }
  }
}
