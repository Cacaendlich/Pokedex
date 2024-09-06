package com.example.pokedex.presenter.ui.details

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokedex.R
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Stat
import com.example.pokedex.data.model.Stats
import com.example.pokedex.data.model.Type
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.battle.HeightWeight
import com.example.pokedex.presenter.ui.battle.TypeCard
import com.example.pokedex.presenter.ui.details.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow
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

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun BodyScope(pokemon: Pokemon) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        GlideImage(
            model = pokemon.imageUrl,
            contentDescription = "Imagem do Pokémon ${pokemon.name}",
            modifier = Modifier
                .size(200.dp)
        )

        Text(
            text = pokemon.name,
            color = White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1, // Limita o texto a uma linha
            overflow = TextOverflow.Ellipsis, // Trunca o texto se ele for muito longo
            modifier = Modifier
                .width(220.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.4f), // Cor da sombra com opacidade
                    offset = Offset(4f, 4f), // Deslocamento da sombra
                    blurRadius = 8f // Raio de desfoque da sombra
                )
            )
        )

        LazyRow {
            items(pokemon.type) { type ->
                TypeCard(type.type.name)
            }
        }

        Spacer(modifier = Modifier.height(1.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 60.dp
            )
        ) {
            HeightWeight(text = "WEIGHT", value = pokemon.weight)
            HeightWeight(text = "HEIGHT", value = pokemon.height)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "STATS",
            color = White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1, // Limita o texto a uma linha
            overflow = TextOverflow.Ellipsis, // Trunca o texto se ele for muito longo
            modifier = Modifier
                .width(200.dp),
            textAlign = TextAlign.Center
        )

        Column {
            pokemon.stats.forEach { stat ->
                if (stat.stat.name != "special-attack" && stat.stat.name != "special-defense") {
                    HorizontalProgressIndicator(progress = stat.base_stat, stat = stat.stat.name)
                }
            }
        }
    }
}

@Composable
fun Heading(context: Context, pokemon: Pokemon){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    Log.e("ClickableImage", "Imagem clicada!")
                    if (context is Activity) {
                        context.finish()
                    }

                },
            painter = painterResource(id = R.drawable.seta_esquerda),
            contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
        )

        Text(
            text = "#${ pokemon.number}",
            color = Black,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

@Composable
fun HorizontalProgressIndicator(progress: Int, stat: String){
    val progressState = remember { Animatable(0f) }

    LaunchedEffect(key1 = progress) {
        progressState.animateTo(
            targetValue = progress.toFloat(),
            animationSpec = tween(
                durationMillis = 1000,  // duração da animação
                delayMillis = 250,      // atraso antes de começar
                easing = FastOutSlowInEasing
            )
        )
    }

    val statColor = when (stat) {
        "hp" -> Blue
        "attack" -> Green
        "defense" -> Red
        "speed" -> Yellow
        else -> Black
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .width(300.dp) // Ajuste a largura da barra aqui
            .height(30.dp)
            .padding(4.dp)
    ) {
        Text(
            text = when(stat){
                "hp" -> "HP   "
                "attack" -> "ATK "
                "defense" -> "DEF "
                "speed" -> "SPD "
                else -> ""
            },
            color = White
        )

        LinearProgressIndicator(
            progress = { progressState.value / 100F },
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .clip(RoundedCornerShape(10.dp))
                .padding(vertical = 2.dp)
                .background(statColor.copy(alpha = 0.3f)),
            color = statColor,
        )

        Text(
            text = if (progress < 99) "  $progress" else "$progress",
            color = White,
            )

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