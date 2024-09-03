package com.example.pokedex.presenter.ui.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.pokemonsList.PokemonList
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow

@Composable
fun MainScreen(onClick: () -> Unit, pokemons: List<Pokemon>, color: Int) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Black, Red),
                    startY = 1000.0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        HeadingPokedex(context = context)
        Box{
            PokemonList(
                pokemons = pokemons,
                color = color
            )
            FavoriteActionButton(
                onClick = onClick,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun HeadingPokedex(context: Context){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = "Pokedex",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Image(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    Log.e("MainScren", "BattleIcon clicado!")
                }
            ,
            painter = painterResource(id = R.drawable.battle_icon),
            contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
        )
    }
}

@Composable
fun FavoriteActionButton(onClick: () -> Unit, modifier: Modifier){
    FloatingActionButton(
        onClick = { onClick() },
        modifier = modifier,
        containerColor = Yellow,
        contentColor = Red,
        shape = CircleShape
    ) {
        Icon(
            imageVector =  Icons.Filled.Favorite,
            contentDescription = "Black heart icon that navigates to the favorites list",
        )
    }
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
//fun GreetingPreview3() {
//    PokedexTheme {
//        MainScreen(onClick = { Log.e("MainScreen", "FavoriteActionButton clicado!") })
//    }
//}