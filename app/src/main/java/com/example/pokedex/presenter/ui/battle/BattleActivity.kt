package com.example.pokedex.presenter.ui.battle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.presenter.ui.battle.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.factory.PokemonsViewModelFactory
import com.example.pokedex.presenter.ui.theme.Black
import com.example.pokedex.presenter.ui.theme.Red
import kotlinx.coroutines.launch

class BattleActivity : ComponentActivity() {
    private lateinit var  battleViewModel: BattleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsViewModelFactory(pokemonApiRepository, pokemonLocalRepository)
        battleViewModel = ViewModelProvider(this, factory)[BattleViewModel::class]

        val pokemonIntent1 = intent.getStringExtra("EXTRA_POKEMON_NAME_1")
        val pokemonIntent2 = intent.getStringExtra("EXTRA_POKEMON_NAME_2")

        setContent {
            PokedexTheme {

                val pokemon1 by battleViewModel.pokemon1StateFlow.collectAsState()
                val pokemon2 by battleViewModel.pokemon2StateFlow.collectAsState()
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(pokemonIntent1, pokemonIntent2) {
                    pokemonIntent1?.let {
                        lifecycleScope.launch {
                            battleViewModel.loadPokemon1(it)
                        }
                    }

                    pokemonIntent2?.let {
                        lifecycleScope.launch {
                            battleViewModel.loadPokemon2(it)
                        }
                    }

                    isLoading = false
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // Ocupa toda a tela
                            .background(
                                Brush.verticalGradient(
                                    listOf(Black, Red, Black),
                                    startY = 200.0f,
                                    endY = 2000.0f,
                                    tileMode = TileMode.Clamp
                                )
                            ), // Mantém a cor de fundo do tema atual
                        contentAlignment = Alignment.Center // Centraliza o conteúdo (CircularProgressIndicator)
                    ) {
                        // Altera a cor para amarelo
                        CircularProgressIndicator(
                            color = Color.Yellow,
                            strokeWidth = 6.dp // Opcional, para ajustar a espessura do indicador
                        )
                    }
                } else {
                    BattleScreen(
                        pokemon1 = battleViewModel.validatePokemon(pokemon1) ,
                        pokemon2 = battleViewModel.validatePokemon(pokemon2) ,
                        goBack = {finish()})
                }


                Log.e("BattleActivity", "Pokemon 1: $pokemon1")
                Log.e("BattleActivity", "Pokemon 2: $pokemon2")

            }
        }
    }
}
