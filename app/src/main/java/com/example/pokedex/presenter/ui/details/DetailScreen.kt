package com.example.pokedex.presenter.ui.details

import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


@Composable
fun DetailScreen(pokemon: Pokemon, color: Int) {
    val pokemonColor = Color(color)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    listOf(pokemonColor, Black),
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
                    if (context is Activity){
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .width(320.dp) // Ajuste a largura da barra aqui
            .height(30.dp)
            .padding(4.dp)
    ) {
        Text(
            text = when(stat){
                "hp" -> "HP "
                "attack" -> "ATK"
                "defense" -> "DEF"
                "speed" -> "SPD"
                else -> ""
            },
            color = White
        )

        Box(
            modifier = Modifier
                .height(20.dp) // Altura máxima da barra
                .width(200.dp) // Largura da barra
                .background(White, shape = RoundedCornerShape(8.dp)) // Cor de fundo da barra
                .clip(RoundedCornerShape(8.dp)) // Bordas arredondadas
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Altura do progresso
                    .fillMaxWidth((progress.toFloat()) / 100)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.BottomStart)
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
            text = progress.toString(),
            color = White
        )

    }

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
        DetailScreen(pokemon = pokemon1, -7815000)
    }
}