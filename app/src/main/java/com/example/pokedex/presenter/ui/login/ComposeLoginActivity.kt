package com.example.pokedex.presenter.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.login.ui.theme.PokedexTheme


@Composable
fun LoginScreen(viewModel: LoginViewModel, goToMainActivity: () -> Unit) {
    val email by rememberSaveable { mutableStateOf("") }
    val password by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(colorResource(R.color.red), colorResource(R.color.yellow)),
                    startY = 1300.0f, //indica que o gradiente começará a partir de 1300 pixels na direção vertical da tela.
                    endY = Float.POSITIVE_INFINITY, //o gradiente se estenderá até o final do contêiner
                    tileMode = TileMode.Clamp //garante que o gradiente não se repita, mas se estenda conforme necessário para preencher o espaço disponível
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.logo_pokebola),
                contentDescription = "Poke ball"
            )
            Text(
                text = "Pokedex",
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(value = remember { mutableStateOf(email) }, text = "E-mail")

            InputText(
                value = remember { mutableStateOf(password) },
                text = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            ButtonLogin {

            }

        }
    }
}

@Composable
fun InputText(value: MutableState<String>, text: String, visualTransformation: VisualTransformation = VisualTransformation.None){
    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        placeholder = { Text(text = text) },
        shape = RoundedCornerShape(20.dp),
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White, // Cor do texto quando focado
            unfocusedTextColor = Color.White, // Cor do texto quando não focado
            focusedContainerColor = colorResource(id = R.color.yellow), // Cor de fundo quando focado
            unfocusedContainerColor = colorResource(id = R.color.yellow), // Cor de fundo quando não focado
            focusedIndicatorColor = Color.Transparent, // Cor da linha inferior quando focado
            unfocusedIndicatorColor = Color.Transparent, // Cor da linha inferior quando não focado
            focusedPlaceholderColor = Color.White, // Cor do placeholder quando focado
            unfocusedPlaceholderColor = Color.White // Cor do placeholder quando nao focado
        ),

        )
}

@Composable
fun ButtonLogin(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.yellow), //Cor do fundo
            contentColor = Color.White, //cor do texto
        )
    ) {
        Text("LOGIN")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PokedexTheme {
        LoginScreen(viewModel = LoginViewModel(), goToMainActivity = {})
    }
}