package com.pritom.cinebyte

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.pritom.cinebyte.ui.theme.CineByteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.component2

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        MovieScreen(viewModel)
                    }
                }
            }
        }
        splashScreen.setKeepOnScreenCondition { keepSplash }
        lifecycleScope.launch {
            if (intent.getStringExtra("receiverData") == null){
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

    LazyColumn {
        moviesByCategory.forEach { (category, movies) ->
            item {
                Text(
                    text = category.category.replace("_"," ").uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red
                )
            }

            items(items = movies){
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}