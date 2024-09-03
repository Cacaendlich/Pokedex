package com.example.pokedex.presenter.ui.pokemonsList

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun PokemonList(pokemons: List<Pokemon>) {

   LazyVerticalGrid(
       columns = GridCells.Fixed(2),
       modifier = Modifier
           .fillMaxSize()
   ) {
       items(pokemons.size){ index->
           PokemonItem(pokemon = pokemons[index])

       }
   }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonItem(pokemon: Pokemon){
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

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { Log.e("PokemonList", "Card clicado!") },
                onLongClick = { Log.e("PokemonList", "Card click longo clicado!") }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(dominantColor.value)
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.favorite_off),
                contentDescription = "heart whith empty, favorite off",
                modifier = Modifier
                    .align(Alignment.End)
                    .size(50.dp)
                    .padding(horizontal = 6.dp)
                    .clickable { Log.e("PokemonList", "favoriteIcon clicado!") }

            )
            GlideImage(
                model = pokemon.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .clickable { Log.e("PokemonList", "pokemonImage clicado!") }
            )

            Spacer(modifier = Modifier.size(2.dp))

            Text(
                text = pokemon.name,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.size(10.dp))

        }
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

    // Recortar a imagem
    return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropRight - cropLeft, cropBottom - cropTop)
}


//val pokemon1 = Pokemon(
//    number = 1,
//    name = "Bulbasaur",
//    height = 7,
//    weight = 69,
//    stats = listOf(
//        Stats(base_stat = 45, stat = Stat(name = "hp")),
//        Stats(base_stat = 49, stat = Stat(name = "attack")),
//        Stats(base_stat = 49, stat = Stat(name = "defense")),
//        Stats(base_stat = 65, stat = Stat(name = "special-attack")),
//        Stats(base_stat = 65, stat = Stat(name = "special-defense")),
//        Stats(base_stat = 45, stat = Stat(name = "speed"))
//    ),
//    type = listOf(
//        PokemonType(slot = 1, type = Type(name = "grass")),
//        PokemonType(slot = 2, type = Type(name = "poison"))
//    ),
//    favorite = false
//)

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview4() {
//    PokedexTheme {
//        PokemonList(pokemons = )
//    }
//}