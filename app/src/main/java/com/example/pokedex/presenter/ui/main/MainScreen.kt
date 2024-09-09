package com.example.pokedex.presenter.ui.main

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
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

@Composable
fun HeadingPokedex(onStartBattle: () -> Unit){
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
            fontSize = 26.sp
        )

        Image(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    onStartBattle()
                }
            ,
            painter = painterResource(id = R.drawable.battle_icon),
            contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
        )
    }
}

@Composable
fun FavoriteActionButton(onFavoriteList: () -> Unit, isFilteredView: Boolean, modifier: Modifier){
    FloatingActionButton(
        onClick = { onFavoriteList() },
        modifier = modifier,
        containerColor = Yellow,
        contentColor = Red,
        shape = CircleShape
    ) {
        Icon(
            imageVector =  if (!isFilteredView) Icons.Filled.Favorite else Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Black heart icon that navigates to the favorites list",
        )
    }
}
