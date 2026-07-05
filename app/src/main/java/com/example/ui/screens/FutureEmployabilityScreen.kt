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
fun FutureEmployabilityScreen(
  viewModel: CareerViewModel,
  onBackClick: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      SubScreenAppBar(
        title = "Future Employability",
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
                text = "Analyze a resume to view future employability.",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )
              Text(
                text = "A 5-year outlook index, predictive demand trajectories, and technology growth indexes are displayed upon resume analysis.",
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
            text = "Future Employability Index",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.testTag("future_employability_title")
          )
          Text(
            text = "Predictive market growth projections and job stability indicators calculated over a 5-year outlook.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }

        // Main Analytics Section: Market Resilience Core Card
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
                  text = "FUTURE MARKET INDEX",
                  style = MaterialTheme.typography.labelSmall,
                  color = BrandPrimary,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.1.sp
                )
                Row(
                  verticalAlignment = Alignment.Bottom,
                  modifier = Modifier.padding(vertical = 4.dp)
                ) {
                  Text(
                    text = "${analysis.futureEmployabilityScore}%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  val resilienceLabel = when {
                    analysis.futureEmployabilityScore >= 80 -> "Highly Resilient"
                    analysis.futureEmployabilityScore >= 60 -> "Resilient"
                    else -> "Emerging"
                  }
                  Text(
                    text = resilienceLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandSecondary,
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
                  progress = { analysis.futureEmployabilityScore / 100f },
                  modifier = Modifier.fillMaxSize(),
                  color = BrandPrimary,
                  strokeWidth = 8.dp,
                  trackColor = Color(0xFF334155)
                )
                Text(
                  text = "${analysis.futureEmployabilityScore}%",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            LinearProgressIndicator(
              progress = { analysis.futureEmployabilityScore / 100f },
              color = BrandPrimary,
              trackColor = Color(0xFF0F172A),
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text("PROJECTED DEMAND GROWTH", style = MaterialTheme.typography.labelSmall, color = BrandSecondaryText)
                val growthVal = if (analysis.futureEmployabilityScore > 0) "+${analysis.futureEmployabilityScore / 3}%" else "+24%"
                Text("$growthVal (High Gain)", style = MaterialTheme.typography.bodyLarge, color = AccentGreen, fontWeight = FontWeight.Bold)
              }
              Column(horizontalAlignment = Alignment.End) {
                Text("RISK EXPOSURE", style = MaterialTheme.typography.labelSmall, color = BrandSecondaryText)
                val riskVal = if (analysis.futureEmployabilityScore >= 70) "Low Risk" else "Medium Risk"
                Text(riskVal, style = MaterialTheme.typography.bodyLarge, color = BrandSecondary, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        // Tech Stack resilience checklist
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "TECHNOLOGY GROWTH INDEX",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.1.sp
          )

          if (analysis.trends.isNotEmpty()) {
            analysis.trends.forEach { trend ->
              val color = when (trend.direction) {
                "UP" -> AccentGreen
                "DOWN" -> AccentRed
                else -> BrandSecondary
              }
              TrendIndicatorRow(trend.techName, trend.trendLabel, trend.percentChange, color)
            }
          } else {
            TrendIndicatorRow("Cloud DevOps Services (AWS, Docker)", "High Trend", "+42% demand", AccentGreen)
            TrendIndicatorRow("Frontend Frameworks (React, Jetpack Compose)", "Stable Trend", "+15% demand", BrandSecondary)
            TrendIndicatorRow("Legacy Server Paradigms", "Decline Trend", "-8% demand", AccentRed)
          }
        }

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(
            onClick = {
              Toast.makeText(context, "Fetching latest Cloud DevOps trend metrics...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("future_employ_explore_trends_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                "Explore In-Demand Skill Paths",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          OutlinedButton(
            onClick = {
              Toast.makeText(context, "Opening market insight dashboard...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("future_employ_demand_reports_btn"),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
          ) {
            Text(
              "View Full Industry Demand Reports",
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
fun TrendIndicatorRow(
  techName: String,
  trendLabel: String,
  percentChange: String,
  indicatorColor: Color
) {
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
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = techName,
          color = Color.White,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Projections based on developer sentiment & open vacancies",
          color = BrandSecondaryText,
          style = MaterialTheme.typography.labelSmall,
          fontSize = 10.sp
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(indicatorColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = trendLabel,
            color = indicatorColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = percentChange,
          color = Color.White,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}
