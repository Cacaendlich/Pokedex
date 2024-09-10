package com.example.pokedex.presenter.ui.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.theme.White

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
