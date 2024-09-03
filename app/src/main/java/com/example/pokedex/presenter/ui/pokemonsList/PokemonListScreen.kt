package com.example.pokedex.presenter.ui.pokemonsList

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.theme.White


@Composable
fun PokemonList(pokemons: List<Pokemon>, color: Int) {

   LazyVerticalGrid(
       columns = GridCells.Fixed(2),
       modifier = Modifier
           .fillMaxSize()
   ) {
       items(pokemons.size){ index->
           PokemonItem(pokemon = pokemons[index], color = color)

       }
   }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonItem(pokemon: Pokemon, color: Int){
    val pokemonColor = Color(color)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { Log.e("PokemonList", "Card clicado!") },
                onLongClick = { Log.e("PokemonList", "Card click longo clicado!") }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(pokemonColor)
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

            Text(
                text = pokemon.name,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
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
//fun GreetingPreview4() {
//    PokedexTheme {
//        PokemonList(pokemons = )
//    }
//}