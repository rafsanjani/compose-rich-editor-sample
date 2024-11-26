package com.foreverrafs.compose_richtext_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.foreverrafs.compose_richtext_sample.ui.theme.ComposeRichTextSampleTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeRichTextSampleTheme {
                setSingletonImageLoaderFactory()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val state = rememberRichTextState()

                    LaunchedEffect(Unit) {
                        state.setHtml("<img src=\"https://placehold.co/600x400/orange/white\">")
                    }
                    RichText(
                        state = state,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .consumeWindowInsets(it)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeRichTextSampleTheme {
        Greeting("Android")
    }
}

@Composable
fun setSingletonImageLoaderFactory() {
    coil3.compose.setSingletonImageLoaderFactory {
        newImageLoader(
            context = it,
            debug = false,
        )
    }
}

private fun newImageLoader(
    context: PlatformContext,
    debug: Boolean = false,
): coil3.ImageLoader {
    return coil3.ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())
            add(SvgDecoder.Factory())
        }
        .memoryCache {
            MemoryCache.Builder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(context, percent = 0.25)
                .build()
        }
        // Show a short crossfade when loading images asynchronously.
        .crossfade(true)
        // Enable logging if this is a debug build.
        .apply {
            if (debug) {
                logger(DebugLogger())
            }
        }
        .build()
}