package com.example.pokedex.presenter.ui.battle.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.details.view.HeightWeight
import com.example.pokedex.presenter.ui.details.view.TypeCard
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BattleBody(
    pokemon: Pokemon,
    showStar: Boolean = false,
    leftColumn: Boolean
){
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
                    listOf(Black, dominantColor.value, Black),
                    startY = 0.0f,
                    endY = 1800.0F,
                    tileMode = TileMode.Clamp
                )
            ),

        verticalArrangement = Arrangement.spacedBy(
            space = 22.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (showStar){
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "Ícone de estrela dourada, representando o Pokémon mais poderoso."
            )
        }else{
            Spacer(modifier = Modifier.height(40.dp))

        }

        GlideImage(
            model = pokemon.imageUrl,
            contentDescription = "Imagem do Pokémon ${pokemon.name}",
            modifier = Modifier
                .size(150.dp)
        )

        Text(
            text = pokemon.name,
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1, // Limita o texto a uma linha
            overflow = TextOverflow.Ellipsis, // Trunca o texto se ele for muito longo
            modifier = Modifier
                .width(200.dp)
            ,
            textAlign = TextAlign.Center
        )

        Text(
            text = "# ${pokemon.number}",
            color = White,
            fontSize = 20.sp,
        )

        LazyRow {
            items(pokemon.type) { type ->
                TypeCard(type.type.name)
            }
        }

        HeightWeight(text = "WEIGHT", value = pokemon.weight)
        HeightWeight(text = "HEIGHT", value = pokemon.height)

        Column{
            pokemon.stats.forEach { stat ->
                if (stat.stat.name != "special-attack" && stat.stat.name != "special-defense")
                    BattleProgressIndicator(progress = stat.base_stat, stat = stat.stat.name, leftColumn = leftColumn)
            }
        }


    }
}

fun isBatter(pokemon1: Pokemon, pokemon2: Pokemon): Boolean{
    val competitor1 = pokemon1.stats.sumOf{
        when(it.stat.name){
            "hp" -> it.base_stat * 1.0
            "attack" -> it.base_stat * 1.5
            "defense" -> it.base_stat * 1.2
            "speed" -> it.base_stat * 1.4
            else -> it.base_stat * 1.0
        }

    }
    val competitor2 = pokemon2.stats.sumOf{
        when(it.stat.name){
            "hp" -> it.base_stat * 1.0
            "attack" -> it.base_stat * 1.5
            "defense" -> it.base_stat * 1.2
            "speed" -> it.base_stat * 1.4
            else -> it.base_stat * 1.0
        }

    }

    return competitor1 > competitor2
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
