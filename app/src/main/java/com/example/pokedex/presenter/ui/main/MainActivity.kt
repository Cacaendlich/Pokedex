package com.example.pokedex.presenter.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.details.PokemonDetailActivity
import com.example.pokedex.presenter.ui.factory.PokemonsViewModelFactory
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var pokemonsListViewModel: PokemonsListViewModel
    private lateinit var favoriteListPokemonViewModel: PokemonFavoriteListViewModel
    private lateinit var mFavoriteList: List<PokemonEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(this)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsViewModelFactory(pokemonApiRepository, pokemonLocalRepository)

        pokemonsListViewModel = ViewModelProvider(this, factory)[PokemonsListViewModel::class.java]
        favoriteListPokemonViewModel = ViewModelProvider(this, factory) [PokemonFavoriteListViewModel::class.java]

        favoriteListPokemonViewModel.loadFavorites()

        favoriteListPokemonViewModel.favoriteList.observe(this) { favoriteList ->
            mFavoriteList = favoriteList
            Log.d(
                "MainActivty",
                "A lista de favoritos foi atualizada para: $favoriteList"
            )
            favoriteListPokemonViewModel.loadAndFilterPokemonsFromFavoriteList(mFavoriteList)
        }

        setContent {
            val pokemons by pokemonsListViewModel.pokemonsState.collectAsState()
            MainScreen(
                onClick = { Log.e("MainScreen", "FavoriteActionButton clicado!") },
                pokemons = pokemons,
                onPokemonImageClick = {pokemon -> goToDetailActivity(pokemon)},
                onLoadMore = { pokemonsListViewModel.loadMorePokemons()},
                onUpdateFavoritesList = { pokemon ->
                    favoriteListPokemonViewModel.updateFavoritesList(pokemon, mFavoriteList)
                }
            )
        }

    }
    private fun goToDetailActivity(pokemon: Pokemon) {
        val intent = Intent(this, PokemonDetailActivity::class.java)
        intent.putExtra("EXTRA_POKEMON_NAME", pokemon.name)
        startActivity(intent)
    }

}