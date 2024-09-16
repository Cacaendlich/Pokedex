package com.example.pokedex.presenter.ui.details.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.presenter.ui.theme.White

@Composable
fun HeightWeight(text: String, value: Int){
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = (value / 10.0).toString(),
            color = White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            color = White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}