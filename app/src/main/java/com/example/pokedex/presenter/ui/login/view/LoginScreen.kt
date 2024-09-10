package com.example.pokedex.presenter.ui.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import com.example.pokedex.presenter.ui.theme.White


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