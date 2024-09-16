package com.example.pokedex.presenter.ui.details.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Bug
import com.example.pokedex.presenter.ui.theme.Dark
import com.example.pokedex.presenter.ui.theme.Dragon
import com.example.pokedex.presenter.ui.theme.Electric
import com.example.pokedex.presenter.ui.theme.Fairy
import com.example.pokedex.presenter.ui.theme.Fighting
import com.example.pokedex.presenter.ui.theme.Fire
import com.example.pokedex.presenter.ui.theme.Flying
import com.example.pokedex.presenter.ui.theme.Ghost
import com.example.pokedex.presenter.ui.theme.Grass
import com.example.pokedex.presenter.ui.theme.Ground
import com.example.pokedex.presenter.ui.theme.Ice
import com.example.pokedex.presenter.ui.theme.Normal
import com.example.pokedex.presenter.ui.theme.Poison
import com.example.pokedex.presenter.ui.theme.Psychic
import com.example.pokedex.presenter.ui.theme.Rock
import com.example.pokedex.presenter.ui.theme.Steel
import com.example.pokedex.presenter.ui.theme.Water
import com.example.pokedex.presenter.ui.theme.White

@Composable
fun TypeCard(typeName: String) {
    Row (
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .background(
                when (typeName) {
                    "bug" -> Bug
                    "dark" -> Dark
                    "dragon" -> Dragon
                    "electric" -> Electric
                    "fairy" -> Fairy
                    "fighting" -> Fighting
                    "fire" -> Fire
                    "flying" -> Flying
                    "ghost" -> Ghost
                    "grass" -> Grass
                    "ground" -> Ground
                    "ice" -> Ice
                    "normal" -> Normal
                    "poison" -> Poison
                    "psychic" -> Psychic
                    "rock" -> Rock
                    "steel" -> Steel
                    "water" -> Water
                    else -> Black
                },
                shape = RoundedCornerShape(18.dp)
            )
            .padding(6.dp)

    ){
        Text(
            text = typeName,
            color = White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f), // Cor da sombra com opacidade
                    offset = Offset(4f, 4f), // Deslocamento da sombra
                    blurRadius = 8f // Raio de desfoque da sombra
                )
            )
        )
    }
}