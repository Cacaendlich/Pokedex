package com.example.pokedex.presenter.ui.details.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow

@Composable
fun DetailProgressIndicator(progress: Int, stat: String){
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
        "hp" -> Blue
        "attack" -> Green
        "defense" -> Red
        "speed" -> Yellow
        else -> Black
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .width(300.dp) // Ajuste a largura da barra aqui
            .height(30.dp)
            .padding(4.dp)
    ) {
        Text(
            text = when(stat){
                "hp" -> "HP   "
                "attack" -> "ATK "
                "defense" -> "DEF "
                "speed" -> "SPD "
                else -> ""
            },
            color = White
        )

        LinearProgressIndicator(
            progress = { progressState.value / 100F },
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .clip(RoundedCornerShape(10.dp))
                .padding(vertical = 2.dp)
                .background(statColor.copy(alpha = 0.3f)),
            color = statColor,
        )

        Text(
            text = if (progress < 99) "  $progress" else "$progress",
            color = White
        )

    }

}