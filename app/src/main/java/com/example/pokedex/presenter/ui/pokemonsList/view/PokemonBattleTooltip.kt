package com.example.pokedex.presenter.ui.pokemonsList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PokemonBattleTooltip(
    tip: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = tip,
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp),
            color = Color.White,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

