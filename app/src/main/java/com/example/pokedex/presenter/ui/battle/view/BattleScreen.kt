package com.example.pokedex.presenter.ui.battle.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.R
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Stat
import com.example.pokedex.data.model.Stats
import com.example.pokedex.data.model.Type
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.PokedexTheme

@Composable
fun BattleScreen(
    pokemon1: Pokemon,
    pokemon2: Pokemon,
    goBack: () -> Unit
) {
    val winnerPokemon = if (isBatter(pokemon1, pokemon2)) pokemon1 else pokemon2
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Black)
    ){
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 10.dp)
                .clickable {
                    Log.e("ClickableImage", "Imagem clicada!")
                    goBack()
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
            BattleBody(pokemon1, pokemon1 == winnerPokemon, leftColumn = true)
            BattleBody(pokemon2, pokemon2 == winnerPokemon, leftColumn = false)
        }
    }

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
        BattleScreen(
            pokemon1 = pokemon1,
            pokemon2 = pokemon2,
            goBack = {})
    }
}