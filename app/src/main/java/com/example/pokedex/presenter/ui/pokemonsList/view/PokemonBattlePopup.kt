package com.example.pokedex.presenter.ui.pokemonsList.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow


@Composable
fun PokemonBattlePopup(
    visible: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero
) {
    if (visible){
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(offset.x.toInt(), offset.y.toInt())
        ) {
            Box(
                modifier = modifier
                    .background(
                        color = Yellow,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(8.dp)
            ){
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.Center
               ) {
                    Text(
                        text = text,
                        color = White,
                        fontSize = 14.sp
                    )

                    Image(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(id = R.drawable.battle_icon),
                        contentDescription = "Ícone de seta vermelha apontando para a esquerda, utilizado para retornar à tela anterior."
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    PokedexTheme {
        PokemonBattlePopup(true, "Click to battle here:")
    }
}

