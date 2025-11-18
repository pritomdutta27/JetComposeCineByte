package com.pritom.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun MoviePosterItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://image.tmdb.org/t/p/original/kGLgaDrYWmTAdRFzGP5pBquRnhO.jpg",
    movieName: String = "Stranger Things",
) {
    val offset = Offset(2.0f, 2.0f)

    Column(modifier = Modifier.width(150.dp)) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(.674f)
                .background(color = Color.Black)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .matchParentSize(),
                contentDescription = "slider item",
                contentScale = ContentScale.Crop
            )
        }


        Text(
            text = movieName,
            modifier = Modifier.padding(4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.W500,
            style = TextStyle(
                fontSize = 16.sp,
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
fun MoviePosterItemPreview() {
    MoviePosterItem()
}