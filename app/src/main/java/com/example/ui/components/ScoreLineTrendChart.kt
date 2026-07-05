package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrandSecondary

@Composable
fun ScoreLineTrendChart(
  scores: List<Float>,
  modifier: Modifier = Modifier,
  lineColor: Color = BrandSecondary
) {
  val animProgress = remember { Animatable(0f) }

  LaunchedEffect(scores) {
    animProgress.animateTo(1f, tween(1000))
  }

  Canvas(modifier = modifier) {
    if (scores.size < 2) return@Canvas

    val width = size.width
    val height = size.height

    val maxScore = 100f
    val minScore = 50f
    val range = maxScore - minScore

    val points = scores.mapIndexed { index, score ->
      val x = (index.toFloat() / (scores.size - 1)) * width
      // Invert Y coordinate so 100 is at top and 50 is at bottom
      val normalizedY = (score - minScore) / range
      val y = height - (normalizedY * height * 0.8f) - (height * 0.1f)
      Offset(x, y)
    }

    val path = Path()
    path.moveTo(points.first().x, points.first().y)

    // Create a smooth cubic curve
    for (i in 0 until points.size - 1) {
      val p0 = points[i]
      val p1 = points[i + 1]
      val controlX1 = p0.x + (p1.x - p0.x) / 2
      val controlY1 = p0.y
      val controlX2 = p0.x + (p1.x - p0.x) / 2
      val controlY2 = p1.y

      path.cubicTo(
        controlX1, controlY1,
        controlX2, controlY2,
        p1.x, p1.y
      )
    }

    // Partially animate drawing using alpha or coordinate scaling
    val fillPath = Path().apply {
      addPath(path)
      lineTo(width, height)
      lineTo(0f, height)
      close()
    }

    // Gradient fill under the curve
    drawPath(
      path = fillPath,
      brush = Brush.verticalGradient(
        colors = listOf(
          lineColor.copy(alpha = 0.25f * animProgress.value),
          Color.Transparent
        )
      )
    )

    // Draw the main line
    drawPath(
      path = path,
      color = lineColor.copy(alpha = animProgress.value),
      style = Stroke(width = 3.dp.toPx())
    )

    // Draw active indicator dot on the final point
    val lastPoint = points.last()
    drawCircle(
      color = lineColor,
      radius = 5.dp.toPx(),
      center = lastPoint
    )
    drawCircle(
      color = Color.White,
      radius = 2.5.dp.toPx(),
      center = lastPoint
    )
  }
}
