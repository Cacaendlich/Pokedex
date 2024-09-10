package com.example.pokedex.presenter.ui.battle.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.StatusAtk
import com.example.pokedex.presenter.ui.theme.StatusDef
import com.example.pokedex.presenter.ui.theme.StatusHP
import com.example.pokedex.presenter.ui.theme.StatusSpd
import com.example.pokedex.presenter.ui.theme.TextAtk
import com.example.pokedex.presenter.ui.theme.TextDef
import com.example.pokedex.presenter.ui.theme.TextHP
import com.example.pokedex.presenter.ui.theme.TextSpd

@Composable
fun BattleProgressIndicator(progress: Int, stat: String, leftColumn: Boolean){
    val progressState = remember { Animatable(0f) }

    LaunchedEffect(key1 = progress) {
        progressState.animateTo(
            targetValue = progress.toFloat(),
            animationSpec = tween(
                durationMillis = 1000,  // duração da animação
                delayMillis = 250,      // atraso antes de começar
                easing = FastOutSlowInEasing
            )
        )
    }

    val statColor = when (stat) {
        "hp" -> StatusHP
        "attack" -> StatusAtk
        "defense" -> StatusDef
        "speed" -> StatusSpd
        else -> Black
    }

    val statTextColor = when (stat) {
        "hp" -> TextHP
        "attack" -> TextAtk
        "defense" -> TextDef
        "speed" -> TextSpd
        else -> Black
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .width(150.dp) // Ajuste a largura da barra aqui
            .height(30.dp)
            .padding(4.dp)
    ) {

        if (leftColumn){
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .graphicsLayer(scaleX = -1f) // Espelha horizontalmente
            ){
                LinearProgressIndicator(
                    progress = { progressState.value / 100F },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp)
                        .background(statColor.copy(alpha = 0.3f)),
                    color = statColor,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = when (stat) {
                            "hp" -> "HP"
                            "attack" -> "ATK"
                            "defense" -> "DEF"
                            "speed" -> "SPD"
                            else -> ""
                        },
                        modifier = Modifier
                            .graphicsLayer(scaleX = -1F),
                        color = statTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$progress",
                        modifier = Modifier
                            .graphicsLayer(scaleX = -1F)
                        ,
                        color = statTextColor,
                        fontSize = 12.sp
                    )
                }
            }
        } else{
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
            )
            {
                LinearProgressIndicator(
                    progress = { progressState.value / 100F },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp)
                        .background(statColor.copy(alpha = 0.3f)),
                    color = statColor,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = when (stat) {
                            "hp" -> "HP"
                            "attack" -> "ATK"
                            "defense" -> "DEF"
                            "speed" -> "SPD"
                            else -> ""
                        },
                        color = statTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$progress",
                        color = statTextColor,
                        fontSize = 12.sp
                    )
                }
            }
        }



    }

}