package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel

@Composable
fun ProfileScreen(
  viewModel: CareerViewModel,
  onNavigateToUpload: () -> Unit,
  onNavigateToDashboard: () -> Unit
) {
  val analysis by viewModel.analysisState.collectAsState()
  val userName by viewModel.userName.collectAsState()
  val jobTitle by viewModel.jobTitle.collectAsState()
  val totalScans by viewModel.totalScans.collectAsState()
  val topScore by viewModel.topScore.collectAsState()
  val badges by viewModel.badges.collectAsState()
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BrandBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 24.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      // Top Profile Config Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Profile",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )

        Box(
          modifier = Modifier
            .size(36.dp)
            .background(Color(0xFF334155).copy(alpha = 0.5f), CircleShape)
            .clickable { /* Simulate settings */ },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // Large Alex Carter Profile Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrandCardBg),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Profile Avatar Circle with Gradient ring
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(Color(0xFF2563EB).copy(alpha = 0.2f))
              .border(2.dp, BrandSecondary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            // Elegant modern avatar placeholder
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = BrandSecondary,
              modifier = Modifier.size(44.dp)
            )
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = userName.ifEmpty { "Candidate" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit name",
                tint = BrandSecondaryText,
                modifier = Modifier.size(14.dp)
              )
            }
            Text(
              text = jobTitle.ifEmpty { "Job Seeker" },
              style = MaterialTheme.typography.bodyMedium,
              color = BrandSecondaryText
            )
          }

          Divider(color = Color(0xFF334155), thickness = 1.dp)

          // 3-way Profile Statistics (Scans, Score, Badges)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            ProfileStatColumn("$totalScans", "SCANS")
            ProfileStatIntermediateDivider()
            ProfileStatColumn("$topScore", "TOP SCORE")
            ProfileStatIntermediateDivider()
            ProfileStatColumn("${badges.size}", "BADGES")
          }
        }
      }

      // Drawer Rows (Resume History, Saved Roadmaps, Achievements, Settings)
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileDrawerItem(
          title = "Resume History",
          subtitle = "View past scans and score progress",
          icon = Icons.Default.Restore,
          onClick = onNavigateToUpload
        )

        ProfileDrawerItem(
          title = "Active Roadmaps",
          subtitle = if (viewModel.roadmap.value.isNotEmpty()) "1 active learning path" else "No active learning paths",
          icon = Icons.Default.Map,
          onClick = onNavigateToDashboard
        )

        ProfileDrawerItem(
          title = "Achievements",
          subtitle = "${badges.filter { !it.isLocked }.size} badges earned",
          icon = Icons.Default.EmojiEvents,
          onClick = onNavigateToDashboard
        )
      }

      // Stylized Logout clicker
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            // Simulated state reset
          }
          .testTag("logout_button"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEF).copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFFFB5C2).copy(alpha = 0.1f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            tint = Color(0xFFEF4444)
          )
          Text(
            text = "Sign Out",
            color = Color(0xFFEF4444),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        com.example.ui.components.VersaCareerLogo(
          modifier = Modifier.size(24.dp),
          showBackground = false
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "VersaCareer AI",
          style = MaterialTheme.typography.labelLarge,
          color = BrandSecondaryText,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun ProfileStatColumn(value: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Black,
      color = Color.White
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = BrandSecondaryText,
      fontSize = 9.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun ProfileStatIntermediateDivider() {
  Box(
    modifier = Modifier
      .width(1.dp)
      .height(24.dp)
      .background(Color(0xFF334155))
  )
}

@Composable
fun ProfileDrawerItem(
  title: String,
  subtitle: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    colors = CardDefaults.cardColors(containerColor = BrandCardBg),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(18.dp)
          )
        }

        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = BrandSecondaryText
          )
        }
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = BrandSecondaryText,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}
