package com.example.pokedex.presenter.ui.details.view

import android.app.Activity
import android.content.Context
import android.util.Log
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
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.theme.Black

@Composable
fun Heading(context: Context, pokemon: Pokemon){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    Log.e("ClickableImage", "Imagem clicada!")
                    if (context is Activity) {
                        context.finish()
                    }

                },
            painter = painterResource(id = R.drawable.seta_esquerda),
            contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
        )

        Text(
            text = "#${ pokemon.number}",
            color = Black,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}