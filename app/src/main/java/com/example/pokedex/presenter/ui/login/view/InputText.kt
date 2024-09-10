package com.example.pokedex.presenter.ui.login.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pokedex.presenter.ui.theme.Transparent
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow

@Composable
fun InputText(value: String, onValueChange: (String) -> Unit, text: String, visualTransformation: VisualTransformation = VisualTransformation.None){
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = text) },
        shape = RoundedCornerShape(20.dp),
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedTextColor = White, // Cor do texto quando focado
            unfocusedTextColor = White, // Cor do texto quando não focado
            focusedContainerColor = Yellow, // Cor de fundo quando focado
            unfocusedContainerColor = Yellow, // Cor de fundo quando não focado
            focusedIndicatorColor = Transparent, // Cor da linha inferior quando focado
            unfocusedIndicatorColor = Transparent, // Cor da linha inferior quando não focado
            focusedPlaceholderColor = White, // Cor do placeholder quando focado
            unfocusedPlaceholderColor = White // Cor do placeholder quando nao focado
        ),

        )
}