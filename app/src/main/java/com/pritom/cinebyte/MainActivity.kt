package com.pritom.cinebyte

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.pritom.cinebyte.ui.theme.CineByteTheme
import com.pritom.designsystem.components.MoviePosterItem
import com.pritom.designsystem.components.MovieSliderItem
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.model.MoviePosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var keepSplash = true


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
// Keep the splash screen visible for this Activity.
        setContent {
            CineByteTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black)
                ) {

                    Box {
                        MovieSliderItem()
                        MovieScreen(viewModel)
                    }

                }
            }
        }
        splashScreen.setKeepOnScreenCondition { keepSplash }
        lifecycleScope.launch {
            if (intent.getStringExtra("receiverData") == null) {
                delay(4500)
            }
            keepSplash = false
        }
    }
}


@Composable
fun MovieScreen(viewModel: MainViewModel) {

    val moviesByCategory by viewModel.moviesByCategory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllMovies()
    }


    Column(modifier = Modifier.padding(top = 200.dp)) {
        moviesByCategory.forEach { (category, movies) ->

            if (category.position == MoviePosition.First) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp), // Adds spacing between items
                    contentPadding = PaddingValues(horizontal = 10.dp) // Adds padding to the start/end of the list
                ) {
                    items(items = movies) { movie ->
                        MoviePosterItem(movieName = movie.title, imageUrl = movie.posterUrl)
                    }
                }
            } else {
                LazyColumn {
                    item {
                        Text(
                            text = category.category.replace("_", " ").uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red
                        )
                    }

                    items(items = movies) {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}
