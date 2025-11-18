package com.pritom.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun MoviePosterListItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://image.tmdb.org/t/p/original/kGLgaDrYWmTAdRFzGP5pBquRnhO.jpg",
    movieName: String = "Stranger Things",
    movieOverView: String = "40-year-old single mom Solène begins an unexpected romance with 24-year-old Hayes Campbell, the lead singer of August Moon",
    rating: String = "2.4",
){

    val offset = Offset(2.0f, 2.0f)

    Row(modifier = modifier.height(160.dp)) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .clip(RoundedCornerShape(9.dp))
                .aspectRatio(9f/16f),
            contentDescription = "slider item",
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(start = 10.dp).fillMaxHeight().weight(1f),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = movieName,
                modifier = Modifier.padding(4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W500,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    letterSpacing = 1.1.sp,
                    shadow = Shadow(
                        color = Color.DarkGray,
                        offset = offset,
                        blurRadius = 3f
                    )
                )
            )

            Text(
                text = movieOverView,
                modifier = Modifier.padding(4.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    letterSpacing = 1.1.sp,
                    shadow = Shadow(
                        color = Color.DarkGray,
                        offset = offset,
                        blurRadius = 3f
                    )
                )
            )

            RatingMovie(rating = rating)
        }

    }

}


@Preview
@Composable
fun MoviePosterListItemPreview(){
    MoviePosterListItem()
}