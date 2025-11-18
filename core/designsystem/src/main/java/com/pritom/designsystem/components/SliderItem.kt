package com.pritom.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)

    ) {

        Image(
            modifier = Modifier
                .matchParentSize()
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

