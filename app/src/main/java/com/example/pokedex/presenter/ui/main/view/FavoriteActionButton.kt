package com.example.pokedex.presenter.ui.main.view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.Yellow

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