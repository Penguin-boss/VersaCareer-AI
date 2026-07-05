package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.provider.OpenableColumns
import android.net.Uri
import android.content.Context

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadScreen(
  viewModel: CareerViewModel,
  onNavigateToAnalysis: () -> Unit
) {
  val resumeState by viewModel.resumeState.collectAsState()
  val analysisState by viewModel.analysisState.collectAsState()
  val history by viewModel.history.collectAsState()
  val context = LocalContext.current

  val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
  ) { uri: Uri? ->
    uri?.let {
      val (name, size) = getFileMetadata(context, it)
      viewModel.selectRealFile(context, name, size, it.toString())
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BrandBackground)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 24.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Title & Subtitle
      item {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Upload Resume",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Analyze your document with clean, responsive parsing.",
            style = MaterialTheme.typography.labelMedium,
            color = BrandSecondaryText
          )
        }
      }

      // Upload Zone (Dashed / Dotted look)
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(BrandCardBg.copy(alpha = 0.5f))
            .border(
              width = 1.dp,
              brush = Brush.linearGradient(listOf(BrandPrimary.copy(alpha = 0.5f), BrandSecondary.copy(alpha = 0.5f))),
              shape = RoundedCornerShape(20.dp)
            )
            .clickable {
              filePickerLauncher.launch(
                arrayOf(
                  "application/pdf",
                  "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                  "application/msword"
                )
              )
            }
            .testTag("upload_drop_zone"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(BrandSecondary.copy(alpha = 0.1f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = BrandSecondary,
                modifier = Modifier.size(24.dp)
              )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                "Select Resume File",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                "PDF, DOC or DOCX up to 5MB",
                color = BrandSecondaryText,
                style = MaterialTheme.typography.labelSmall
              )
            }

            Button(
              onClick = {
                filePickerLauncher.launch(
                  arrayOf(
                    "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/msword"
                  )
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("browse_files_button")
            ) {
              Text(
                "Browse Files",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Display "No resume uploaded" when IDLE
      if (resumeState.status == UploadStatus.IDLE) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth().testTag("unuploaded_state_card"),
            colors = CardDefaults.cardColors(containerColor = BrandCardBg.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.4f))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = null,
                tint = BrandSecondaryText,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "No resume uploaded",
                color = BrandSecondaryText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // Progress Tracker & Metrics Overlay (After selection)
      item {
        AnimatedVisibility(
          visible = resumeState.status != UploadStatus.IDLE,
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BrandCardBg),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF334155))
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(
                    imageVector = when (resumeState.status) {
                      UploadStatus.UPLOADING -> Icons.Default.Cached
                      UploadStatus.ANALYZING -> Icons.Default.QueryStats
                      UploadStatus.COMPLETED -> Icons.Default.CheckCircle
                      else -> Icons.Default.AttachFile
                    },
                    contentDescription = null,
                    tint = if (resumeState.status == UploadStatus.COMPLETED) AccentGreen else BrandSecondary,
                    modifier = Modifier.size(20.dp)
                  )
                  Column {
                    Text(
                      text = resumeState.fileName,
                      color = Color.White,
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.Bold,
                      maxLines = 1
                    )
                    Text(
                      text = "Size: ${resumeState.fileSize}",
                      color = BrandSecondaryText,
                      style = MaterialTheme.typography.labelSmall
                    )
                  }
                }
                
                // Upload Status Indicator label
                Text(
                  text = when (resumeState.status) {
                    UploadStatus.UPLOADING -> "Uploading"
                    UploadStatus.ANALYZING -> "Processing"
                    UploadStatus.COMPLETED -> "Ready for Analysis"
                    else -> "Idle"
                  },
                  color = if (resumeState.status == UploadStatus.COMPLETED) AccentGreen else BrandSecondary,
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier
                    .background(
                      color = (if (resumeState.status == UploadStatus.COMPLETED) AccentGreen else BrandSecondary).copy(alpha = 0.15f),
                      shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }

              LinearProgressIndicator(
                progress = { resumeState.progress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(CircleShape),
                color = if (resumeState.status == UploadStatus.COMPLETED) AccentGreen else BrandSecondary,
                trackColor = Color(0xFF0F172A)
              )

              if (resumeState.status == UploadStatus.COMPLETED) {
                Button(
                  onClick = {
                    viewModel.triggerAnalysis(context) // Perform actual structured analysis
                    onNavigateToAnalysis() // Navigate to Resume analysis subscreen
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("analyze_resume_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Icon(
                      imageVector = Icons.Default.AutoAwesome,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      "Ready for Analysis", // Display "Ready for Analysis"
                      color = Color.White,
                      style = MaterialTheme.typography.labelLarge,
                      fontWeight = FontWeight.Black
                    )
                  }
                }
              } else {
                Text(
                  text = "${(resumeState.progress * 100).toInt()}% completed",
                  color = BrandSecondaryText,
                  style = MaterialTheme.typography.labelSmall
                )
              }
            }
          }
        }
      }

      // Recent uploads section label
      item {
        Text(
          "Recent Uploads",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      // Recent uploads list
      items(history) { record ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BrandCardBg)
            .border(1.dp, Color(0xFF334155).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable {
              onNavigateToAnalysis()
            }
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.InsertDriveFile,
                contentDescription = null,
                tint = BrandSecondary,
                modifier = Modifier.size(18.dp)
              )
            }

            Column {
              Text(
                record.fileName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                record.date,
                color = BrandSecondaryText,
                style = MaterialTheme.typography.labelSmall
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF22D3EE).copy(alpha = 0.1f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              "${record.score}/100",
              color = BrandSecondary,
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.labelSmall
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

private fun getFileMetadata(context: Context, uri: Uri): Pair<String, String> {
  var name = "unknown_resume.pdf"
  var size = "0 KB"
  try {
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
      val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
      val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
      if (cursor.moveToFirst()) {
        if (nameIndex != -1) name = cursor.getString(nameIndex)
        if (sizeIndex != -1) {
          val bytes = cursor.getLong(sizeIndex)
          size = formatFileSize(bytes)
        }
      }
    }
  } catch (e: Exception) {
    // Fallback names
  }
  return Pair(name, size)
}

private fun formatFileSize(sizeInBytes: Long): String {
  if (sizeInBytes <= 0) return "0 B"
  val units = arrayOf("B", "KB", "MB", "GB")
  val digitGroups = (Math.log10(sizeInBytes.toDouble()) / Math.log10(1024.0)).toInt()
  return String.format("%.1f %s", sizeInBytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}
