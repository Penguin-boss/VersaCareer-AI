package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.BrandSecondaryText
import kotlin.math.cos
import kotlin.math.sin

data class RadarData(
  val label: String,
  val value: Float // 0f to 1.0f
)

@Composable
fun SkillRadarChart(
  axes: List<RadarData>,
  modifier: Modifier = Modifier,
  primaryColor: Color = BrandPrimary,
  secondaryColor: Color = BrandSecondary,
  fillPercent: Float = 0.5f
) {
  val animationProgress = remember { Animatable(0f) }
  LaunchedEffect(axes) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(1200)
    )
  }

  val textMeasurer = rememberTextMeasurer()

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)
    ) {
      val center = Offset(size.width / 2f, size.height / 2f)
      val radius = size.width.coerceAtMost(size.height) / 2f

      val numAxes = axes.size
      if (numAxes == 0) return@Canvas

      // Draw background grid concentric rings (e.g., 4 levels)
      val numRings = 4
      for (i in 1..numRings) {
        val ringRadius = radius * (i.toFloat() / numRings)
        val ringPath = Path()
        for (j in 0 until numAxes) {
          val angle = (j * 2 * Math.PI / numAxes) - Math.PI / 2
          val x = center.x + ringRadius * cos(angle).toFloat()
          val y = center.y + ringRadius * sin(angle).toFloat()
          if (j == 0) {
            ringPath.moveTo(x, y)
          } else {
            ringPath.lineTo(x, y)
          }
        }
        ringPath.close()
        drawPath(
          path = ringPath,
          color = Color(0xFF334155).copy(alpha = 0.4f),
          style = Stroke(width = 1.dp.toPx())
        )
      }

      // Draw radial axis lines
      for (j in 0 until numAxes) {
        val angle = (j * 2 * Math.PI / numAxes) - Math.PI / 2
        val endX = center.x + radius * cos(angle).toFloat()
        val endY = center.y + radius * sin(angle).toFloat()
        drawLine(
          color = Color(0xFF334155).copy(alpha = 0.5f),
          start = center,
          end = Offset(endX, endY),
          strokeWidth = 1.dp.toPx()
        )

        // Draw Axes Labels
        val labelData = axes[j]
        val labelRadius = radius + 22.dp.toPx()
        val labelX = center.x + labelRadius * cos(angle).toFloat()
        val labelY = center.y + labelRadius * sin(angle).toFloat()

        val textLayout = textMeasurer.measure(
          text = labelData.label,
          style = TextStyle(
            color = BrandSecondaryText,
            fontSize = 9.sp
          )
        )
        // Center text on the label coordinate
        drawText(
          textLayoutResult = textLayout,
          topLeft = Offset(
            x = labelX - textLayout.size.width / 2f,
            y = labelY - textLayout.size.height / 2f
          )
        )
      }

      // Draw data polygon
      val polygonPath = Path()
      for (j in 0 until numAxes) {
        val angle = (j * 2 * Math.PI / numAxes) - Math.PI / 2
        val dataVal = axes[j].value * animationProgress.value
        val dataRadius = dataVal * radius
        val x = center.x + dataRadius * cos(angle).toFloat()
        val y = center.y + dataRadius * sin(angle).toFloat()
        if (j == 0) {
          polygonPath.moveTo(x, y)
        } else {
          polygonPath.lineTo(x, y)
        }
      }
      polygonPath.close()

      // Fill in translucent primary/secondary gradient
      drawPath(
        path = polygonPath,
        color = secondaryColor.copy(alpha = 0.25f)
      )

      // Outline the data shape
      drawPath(
        path = polygonPath,
        color = primaryColor,
        style = Stroke(width = 2.dp.toPx())
      )

      // Draw little glowing points at the corners of active values
      for (j in 0 until numAxes) {
        val angle = (j * 2 * Math.PI / numAxes) - Math.PI / 2
        val dataVal = axes[j].value * animationProgress.value
        val dataRadius = dataVal * radius
        val x = center.x + dataRadius * cos(angle).toFloat()
        val y = center.y + dataRadius * sin(angle).toFloat()

        drawCircle(
          color = secondaryColor,
          radius = 4.dp.toPx(),
          center = Offset(x, y)
        )
        drawCircle(
          color = Color.White,
          radius = 2.dp.toPx(),
          center = Offset(x, y)
        )
      }
    }
  }
}
