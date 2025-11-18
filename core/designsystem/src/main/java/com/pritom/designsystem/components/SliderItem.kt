package com.pritom.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pritom.designsystem.R

@Composable
fun MovieSliderItem(
    modifier: Modifier = Modifier,
    image: Int = R.drawable.item_slider,
) {

    val infiniteTransition = rememberInfiniteTransition(label = "zoomAnimation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f, // 30% zoom
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)

    ) {

        Image(
            modifier = Modifier
                .matchParentSize()
                .scale(scale)
                .aspectRatio(9f / 16f),
            painter = painterResource(id = image),
            contentDescription = "slider item",
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .aspectRatio(9f / 16f)// Matches the size of the Image
                .background(
                    brush = Brush.verticalGradient(
                        // Define colors: Transparent at the top, black at the bottom
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.9f),
                            Color.Black
                        ),
                        startY = 800f, // Start gradient at the top
                        endY = Float.POSITIVE_INFINITY // End gradient at the bottom edge
                    )
                )
        )
    }

}

@Preview
@Composable
fun SliderItemPreview() {
    MovieSliderItem()
}

