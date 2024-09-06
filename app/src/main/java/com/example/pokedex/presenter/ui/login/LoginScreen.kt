package com.example.pokedex.presenter.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.Transparent
import com.example.pokedex.presenter.ui.theme.White
import com.example.pokedex.presenter.ui.theme.Yellow


@Composable
fun LoginScreen(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit
) {

        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Red, Black),
                        startY = 1300.0f, //indica que o gradiente começará a partir de 1300 pixels na direção vertical da tela.
                        endY = Float.POSITIVE_INFINITY, //o gradiente se estenderá até o final do contêiner
                        tileMode = TileMode.Clamp //garante que o gradiente não se repita, mas se estenda conforme necessário para preencher o espaço disponível
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp),
                painter = painterResource(id = R.drawable.logo_pokebola),
                contentDescription = "Poke ball"
            )
            Text(
                text = "Pokedex",
                color = White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(value = email ,onValueChange = onEmailChanged, text = "E-mail")

            InputText(
                value = password,
                onValueChange = onPasswordChanged,
                text = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            ButtonLogin (onClick = onLoginClick)
        }

}

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

@Composable
fun ButtonLogin(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Yellow, //Cor do fundo
            contentColor = White, //cor do texto
        )
    ) {
        Text(
            text= "LOGIN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        email = "",
        password = "",
        onEmailChanged = {},
        onPasswordChanged = {},
        onLoginClick = {}
    )
}