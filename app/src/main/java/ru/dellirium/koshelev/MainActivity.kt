package ru.dellirium.koshelev

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import ru.dellirium.koshelev.ui.theme.KoshelevTheme

@ExperimentalCoilApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoshelevTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }

    @Composable
    fun Main() {
        val quote = viewModel.currentQuote.observeAsState().value
        val quoteIndex = viewModel.currentQuoteIndex.observeAsState().value

        Scaffold(
            topBar = {
                TopBar("Developers' life")
            },
            content = {
                if (quote != null) {
                    Column {
                        ImageQuote(imageUrl = quote.gifURL)
                        ImageText(text = quote.description)
                    }

                }
            },
            bottomBar = {
                BottomButtons(
                    currentIndex = quoteIndex ?: 0,
                    onPreviousPressed = { viewModel.showPrevQuote() },
                    onNextPressed = { viewModel.showNextQuote() }
                )
            }
        )
    }
}

@Composable
fun TopBar(name: String) {
    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .background(color = Color(0xffaaaaaa))
            .padding(top = 24.dp, bottom = 20.dp)
            .fillMaxWidth(),
        fontSize = 22.sp,
        textAlign = TextAlign.Center,
        text = name)
}

@Composable
fun BottomButtons(
    currentIndex: Int,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = currentIndex != 0,
            onClick = onPreviousPressed
        ) {
            Text(text = "Назад")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = onNextPressed
        ) {
            Text(text = "Вперёд")
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageQuote(imageUrl: String) {

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder(LocalContext.current))
            } else {
                add(GifDecoder())
            }
        }
        .build()

    CoilImage(
        imageModel = imageUrl,
        imageLoader = imageLoader,
        shimmerParams = ShimmerParams(
            baseColor = Color(0xFF333333),
            highlightColor = Color(0xFFcccccc)
        ),
        error = painterResource(R.drawable.error),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(220.dp)
    )
}

@Composable
fun ImageText(text: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        text = text,
    )
}
