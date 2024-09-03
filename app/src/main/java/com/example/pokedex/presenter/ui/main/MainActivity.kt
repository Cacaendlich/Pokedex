package com.example.pokedex.presenter.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.factory.PokemonsViewModelFactory
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var pokemonsListViewModel: PokemonsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(this)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsViewModelFactory(pokemonApiRepository, pokemonLocalRepository)

        pokemonsListViewModel = ViewModelProvider(this, factory)[PokemonsListViewModel::class.java]

        setContent {
            val pokemons by pokemonsListViewModel.pokemonsState.collectAsState()
            MainScreen(onClick = { Log.e("MainScreen", "FavoriteActionButton clicado!") }, pokemons,  -7815000)
        }


    }

}