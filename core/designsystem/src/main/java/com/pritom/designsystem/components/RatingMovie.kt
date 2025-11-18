package com.pritom.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pritom.designsystem.icon.CineByteIcons

@Composable
fun RatingMovie(modifier: Modifier = Modifier, rating: String = "7.8") {

    val offset = Offset(2.0f, 2.0f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = CineByteIcons.Star, contentDescription = null,
            tint = Color.Yellow,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = rating,
            maxLines = 1,
            fontWeight = FontWeight.Normal,

            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White,
                letterSpacing = 1.1.sp,
                shadow = Shadow(
                    color = Color.DarkGray,
                    offset = offset,
                    blurRadius = 3f
                )
            )
        )
    }
}

@Preview
@Composable
fun RatingMoviePreview() {
    RatingMovie()
}