package com.example.pokedex.presenter.ui.details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.factory.PokemonsViewModelFactory
import com.example.pokedex.presenter.ui.theme.PokedexTheme
import kotlinx.coroutines.launch

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var pokemonDetailsViewModel : PokemonDetailsViewModel

    private lateinit var mPokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsViewModelFactory(pokemonApiRepository, pokemonLocalRepository)


        pokemonDetailsViewModel = ViewModelProvider(this, factory)[PokemonDetailsViewModel::class.java]

        val pokemonName = intent.getStringExtra("EXTRA_POKEMON_NAME")
        pokemonName?.let {
            lifecycleScope.launch {
               pokemonDetailsViewModel.loadPokemon(pokemonName.toString())
            }
        }

        pokemonDetailsViewModel.pokemonLiveData.observe(this) {
            mPokemon = pokemonDetailsViewModel.validatePokemon(it)

            setContent {
                PokedexTheme {
                    DetailScreen(
                        pokemon = pokemonDetailsViewModel.validatePokemon(mPokemon)
                    )
                }
            }
        }

    }
}
