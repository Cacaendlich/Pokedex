package com.example.pokedex.presenter.ui.pokemonsList.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pokedex.domain.model.Pokemon


@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    onPokemonImageClick: (Pokemon) -> Unit,
    onLoadMore: () -> Unit,
    onUpdateFavoritesList: (Pokemon) -> Unit,
    isSelected: (Pokemon) -> Boolean,
    onSelectPokemon: (Pokemon) -> Unit
)
{

   LazyVerticalGrid(
       columns = GridCells.Fixed(2),
       modifier = Modifier
           .fillMaxSize()
   ) {
       items(pokemons.size){ index->
           PokemonItem(
               pokemon = pokemons[index],
               onPokemonImageClick = onPokemonImageClick,
               onUpdateFavoritesList = onUpdateFavoritesList,
               isSelected = isSelected,
               onSelectPokemon = onSelectPokemon
           )

           if (index == pokemons.size -1){
               onLoadMore()
           }
       }

   }
}