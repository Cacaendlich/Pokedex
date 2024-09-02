package com.example.pokedex.presenter.ui.battle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.battle.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.factory.PokemonsListViewModelFactory
import kotlinx.coroutines.launch

class BattleActivity : ComponentActivity() {
    private lateinit var  battleViewModel: BattleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsListViewModelFactory(pokemonApiRepository, pokemonLocalRepository)
        battleViewModel = ViewModelProvider(this, factory)[BattleViewModel::class]

        val pokemonIntent1 = intent.getStringExtra("EXTRA_POKEMON_NAME_1")
        val pokemonIntent2 = intent.getStringExtra("EXTRA_POKEMON_NAME_2")

        setContent {
            PokedexTheme {

                var pokemon1 by remember { mutableStateOf<Pokemon?>(null) }
                var pokemon2 by remember { mutableStateOf<Pokemon?>(null) }

                LaunchedEffect(pokemonIntent1) {
                    pokemonIntent1?.let {
                        lifecycleScope.launch {
                            battleViewModel.loadPokemon(it)
                            pokemon1 = battleViewModel.pokemonLiveData.value
                        }
                    }
                }

                LaunchedEffect(pokemonIntent2) {
                    pokemonIntent2?.let {
                        lifecycleScope.launch {
                            battleViewModel.loadPokemon(it)
                            pokemon2 = battleViewModel.pokemonLiveData.value
                        }
                    }
                }

                    BattleScreen(pokemon1 = battleViewModel.validatePokemon(pokemon1) , pokemon2 = battleViewModel.validatePokemon(pokemon2) )
            }
        }
    }
}
