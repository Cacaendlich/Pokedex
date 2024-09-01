package com.example.pokedex.presenter.ui.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Stat
import com.example.pokedex.data.model.Stats
import com.example.pokedex.data.model.Type
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.battle.ui.theme.PokedexTheme
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
import com.example.pokedex.presenter.ui.theme.Psychic
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.Rock
import com.example.pokedex.presenter.ui.theme.Steel
import com.example.pokedex.presenter.ui.theme.Water
import com.example.pokedex.presenter.ui.theme.White

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

@Composable
fun BattleScreen(pokemon1: Pokemon, pokemon2: Pokemon) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Black, Red),
                    startY = 1300.0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        PokemonDetail(pokemon1)
        PokemonDetail(pokemon2)
    }
}

@Composable
fun PokemonDetail(
    pokemon: Pokemon
){
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(100.dp),
            painter = painterResource(id = R.drawable.logo_pokebola),
            contentDescription = "Poke ball"
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
                TypeChip(type.type.name)
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
fun TypeChip(typeName: String) {
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
            color = White
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
            text = value.toFloat().toString(),
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        BattleScreen(pokemon1 = pokemon1, pokemon2 = pokemon2)
    }
}