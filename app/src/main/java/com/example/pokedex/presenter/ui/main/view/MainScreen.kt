package com.example.pokedex.presenter.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.pokemonsList.view.PokemonList
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red

@Composable
fun MainScreen(
    onFavoriteList: () -> Unit,
    pokemons: List<Pokemon>,
    onPokemonImageClick: (Pokemon) -> Unit,
    onLoadMore: () -> Unit,
    onUpdateFavoritesList: (Pokemon) -> Unit,
    isFilteredView: Boolean,
    onStartBattle: () -> Unit,
    isSelected: (Pokemon) -> Boolean,
    onSelectPokemon: (Pokemon) -> Unit
) {
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
        HeadingPokedex(onStartBattle = onStartBattle)
        Box{
            PokemonList(
                pokemons = pokemons,
                onPokemonImageClick = onPokemonImageClick,
                onLoadMore =  onLoadMore,
                onUpdateFavoritesList = onUpdateFavoritesList,
                isSelected = isSelected,
                onSelectPokemon = onSelectPokemon
            )
            FavoriteActionButton(
                onFavoriteList = onFavoriteList,
                isFilteredView = isFilteredView,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
