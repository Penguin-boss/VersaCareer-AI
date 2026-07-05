package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp

@Composable
fun VersaCareerLogo(
  modifier: Modifier = Modifier,
  showBackground: Boolean = false
) {
  Canvas(
    modifier = modifier
  ) {
    val w = size.width
    val h = size.height

    val pathColor = Color.White

    // Draw the single, solid, beautiful swooping V official logo path
    val vPath = Path().apply {
      moveTo(w * 0.21f, h * 0.28f)
      lineTo(w * 0.34f, h * 0.28f)
      lineTo(w * 0.51f, h * 0.59f)
      
      // Beautiful inner right arm curve swooping up-right
      cubicTo(
        w * 0.58f, h * 0.45f,
        w * 0.66f, h * 0.32f,
        w * 0.77f, h * 0.28f
      )
      
      // Beautiful outer right arm curve swooping down-left
      quadraticTo(
        w * 0.62f, h * 0.44f,
        w * 0.50f, h * 0.80f
      )
      
      // Left outer edge line straight back to the top-left corner
      lineTo(w * 0.21f, h * 0.28f)
      close()
    }

    drawPath(
      path = vPath,
      color = pathColor
    )
  }
}


