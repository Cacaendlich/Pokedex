package com.example.pokedex.presenter.ui.details.view

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Stat
import com.example.pokedex.data.model.Stats
import com.example.pokedex.data.model.Type
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.details.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.theme.Black
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun DetailScreen(pokemon: Pokemon) {
    val context = LocalContext.current

    val defaultColor = Color.White
    val dominantColor = remember { mutableStateOf(defaultColor) }

    LaunchedEffect(pokemon.imageUrl) {
        withContext(Dispatchers.IO){
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(pokemon.imageUrl)
                .submit()
                .get()

            // Recortar a imagem ao centro antes de extrair a cor dominante
            val croppedBitmap = cropCenteredBitmap(bitmap)

            withContext(Dispatchers.IO) {
                extractDominantColor(croppedBitmap) { color ->
                    dominantColor.value = Color(color)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    listOf(dominantColor.value, Black),
                    startY = 1000.0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        Heading(context, pokemon)
        BodyScope(pokemon)

    }
}
// Função para extrair a cor dominante usando Palette
private fun extractDominantColor(bitmap: Bitmap, onColorExtracted: (Int) -> Unit) {
    Palette.from(bitmap).generate { palette ->
        val dominantColor = palette?.dominantSwatch?.rgb ?: Color.White.toArgb()
        onColorExtracted(dominantColor)
    }
}

private fun cropCenteredBitmap(bitmap: Bitmap): Bitmap {
    // Determinar as dimensões do retângulo de recorte
    val cropLeft = bitmap.width / 4  // 25% da largura da imagem original
    val cropTop = bitmap.height / 4  // 25% da altura da imagem original
    val cropRight = bitmap.width * 3 / 4  // 75% da largura da imagem original
    val cropBottom = bitmap.height * 3 / 4  // 75% da altura da imagem original

    return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropRight - cropLeft, cropBottom - cropTop)
}

val pokemon1 = Pokemon(
    number = 1,
    name = "Bulbasaur",
    height = 7,
    weight = 69,
    stats = listOf(
        Stats(base_stat = 45, stat = Stat(name = "hp")),
        Stats(base_stat = 49, stat = Stat(name = "attack")),
        Stats(base_stat = 49, stat = Stat(name = "defense")),
        Stats(base_stat = 65, stat = Stat(name = "special-attack")),
        Stats(base_stat = 65, stat = Stat(name = "special-defense")),
        Stats(base_stat = 45, stat = Stat(name = "speed"))
    ),
    type = listOf(
        PokemonType(slot = 1, type = Type(name = "grass")),
        PokemonType(slot = 2, type = Type(name = "poison"))
    ),
    favorite = false
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PokedexTheme {
        DetailScreen(pokemon = pokemon1)
    }
}