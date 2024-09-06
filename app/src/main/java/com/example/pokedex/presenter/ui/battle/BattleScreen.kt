package com.example.pokedex.presenter.ui.battle

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color.Companion.Yellow
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
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Bug
import com.example.pokedex.presenter.ui.theme.Dark
import com.example.pokedex.presenter.ui.theme.Dragon
import com.example.pokedex.presenter.ui.theme.Electric
import com.example.pokedex.presenter.ui.theme.Fairy
import com.example.pokedex.presenter.ui.theme.Fighting
import com.example.pokedex.presenter.ui.theme.Fire
import com.example.pokedex.presenter.ui.theme.Flying
import com.example.pokedex.presenter.ui.theme.Ghost
import com.example.pokedex.presenter.ui.theme.Grass
import com.example.pokedex.presenter.ui.theme.Ground
import com.example.pokedex.presenter.ui.theme.Ice
import com.example.pokedex.presenter.ui.theme.Normal
import com.example.pokedex.presenter.ui.theme.Poison
import com.example.pokedex.presenter.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.theme.Psychic
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.Rock
import com.example.pokedex.presenter.ui.theme.Steel
import com.example.pokedex.presenter.ui.theme.Water
import com.example.pokedex.presenter.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BattleScreen(pokemon1: Pokemon, pokemon2: Pokemon) {
    val winnerPokemon = if (isBatter(pokemon1, pokemon2)) pokemon1 else pokemon2
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Black, White, Black),
                    startY = 200.0f,
                    endY = 2000.0f,
                    tileMode = TileMode.Clamp
                )
            )
    ){
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 10.dp)
                .clickable {
                    Log.e("ClickableImage", "Imagem clicada!")
//                    depois ter um finish()
                },
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()

        ) {
            PokemonDetail(pokemon1, pokemon1 == winnerPokemon)
            PokemonDetail(pokemon2, pokemon2 == winnerPokemon)
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonDetail(
    pokemon: Pokemon,
    showStar: Boolean = false
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
                    startY = 100.0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            ),

        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (showStar){
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Ícone de estrela dourada, representando o Pokémon mais poderoso."
            )
        }else{
            Spacer(modifier = Modifier.height(40.dp))

        }

        GlideImage(
            model = pokemon.imageUrl,
            contentDescription = "Imagem do Pokémon ${pokemon.name}",
            modifier = Modifier
                .size(100.dp)
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

        Row{
            pokemon.stats.forEach { stat ->
                if (stat.stat.name != "special-attack" && stat.stat.name != "special-defense")
                VerticalProgressIndicator(progress = stat.base_stat, stat = stat.stat.name)
            }
        }


    }
}

@Composable
fun TypeCard(typeName: String) {
    Row (
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .background(
                when (typeName) {
                    "bug" -> Bug
                    "dark" -> Dark
                    "dragon" -> Dragon
                    "electric" -> Electric
                    "fairy" -> Fairy
                    "fighting" -> Fighting
                    "fire" -> Fire
                    "flying" -> Flying
                    "ghost" -> Ghost
                    "grass" -> Grass
                    "ground" -> Ground
                    "ice" -> Ice
                    "normal" -> Normal
                    "poison" -> Poison
                    "psychic" -> Psychic
                    "rock" -> Rock
                    "steel" -> Steel
                    "water" -> Water
                    else -> Black
                },
                shape = RoundedCornerShape(18.dp)
            )
            .padding(6.dp)

    ){
        Text(
            text = typeName,
            color = White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f), // Cor da sombra com opacidade
                    offset = Offset(4f, 4f), // Deslocamento da sombra
                    blurRadius = 8f // Raio de desfoque da sombra
                )
            )
        )
    }
}

@Composable
fun HeightWeight(text: String, value: Int){
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = (value / 10.0).toString(),
            color = White,
            fontSize = 20.sp,
        )
        Text(
            text = text,
            color = White,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun VerticalProgressIndicator(progress: Int, stat: String){
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(200.dp) // Ajuste a altura da barra aqui
            .width(40.dp) // Ajuste a largura da barra aqui
            .padding(8.dp) // Espaço entre o contorno e o preenchimento
    ) {
        Text(
            text = progress.toString(),
            color = White
        )

        Box(
            modifier = Modifier
                .height(100.dp) // Altura máxima da barra
                .width(20.dp) // Largura da barra
                .background(White, shape = RoundedCornerShape(8.dp)) // Cor de fundo da barra
                .clip(RoundedCornerShape(8.dp)) // Bordas arredondadas
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(progress.toFloat() / 100) // Altura do progresso
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.BottomCenter)
                    .background(
                        color = when (stat) {
                            "hp" -> Blue
                            "attack" -> Green
                            "defense" -> Red
                            "speed" -> Yellow
                            else -> Black
                        }
                    )
            )
        }

        Text(
            text = stat.firstOrNull()?.uppercase().toString(),
            color = White
        )
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

// Mocked Pokemon data
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

val pokemon2 = Pokemon(
    number = 25,
    name = "Pikachu",
    height = 4,
    weight = 60,
    stats = listOf(
        Stats(base_stat = 35, stat = Stat(name = "hp")),
        Stats(base_stat = 55, stat = Stat(name = "attack")),
        Stats(base_stat = 40, stat = Stat(name = "defense")),
        Stats(base_stat = 50, stat = Stat(name = "special-attack")),
        Stats(base_stat = 50, stat = Stat(name = "special-defense")),
        Stats(base_stat = 90, stat = Stat(name = "speed"))
    ),
    type = listOf(
        PokemonType(slot = 1, type = Type(name = "electric"))
    ),
    favorite = false
)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        BattleScreen(pokemon1 = pokemon1, pokemon2 = pokemon2)
    }
}