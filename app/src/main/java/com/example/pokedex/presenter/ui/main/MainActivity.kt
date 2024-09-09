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
import com.example.pokedex.presenter.ui.battle.BattleActivity
import com.example.pokedex.presenter.ui.details.PokemonDetailActivity
import com.example.pokedex.presenter.ui.factory.PokemonsViewModelFactory
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var pokemonsListViewModel: PokemonsListViewModel
    private lateinit var mFavoriteList: List<PokemonEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(this)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsViewModelFactory(pokemonApiRepository, pokemonLocalRepository)

        pokemonsListViewModel = ViewModelProvider(this, factory)[PokemonsListViewModel::class.java]

        pokemonsListViewModel.loadFavorites()

        pokemonsListViewModel.favoriteList.observe(this) { favoriteList ->
            mFavoriteList = favoriteList
            Log.d(
                "MainActivty",
                "A lista de favoritos foi atualizada para: ${favoriteList.map { it.name }}"
            )
            pokemonsListViewModel.loadAndFilterPokemonsFromFavoriteList(mFavoriteList)
        }


        setContent {
            val pokemons by pokemonsListViewModel.pokemonsAllState.collectAsState()

            val favoritePokemons by pokemonsListViewModel.pokemonsFavoriteState.collectAsState()

            val isFilteredView by pokemonsListViewModel.isFilteredView.collectAsState()

            val pokemon1 by pokemonsListViewModel.pokemon1State.collectAsState()
            val pokemon2 by pokemonsListViewModel.pokemon2State.collectAsState()


            val updatePokemon = if (!isFilteredView) {
                pokemons.map { pokemon ->
                    if (mFavoriteList.any { it.name == pokemon.name }) {
                        pokemon.copy(favorite = true)
                    } else {
                        pokemon.copy(favorite = false)
                    }
                }
            } else {
                favoritePokemons.map { pokemon ->
                    if (mFavoriteList.any { it.name == pokemon.name }) {
                        pokemon.copy(favorite = true)
                    } else {
                        pokemon.copy(favorite = false)
                    }
                }
            }

            MainScreen(
                onFavoriteList = { pokemonsListViewModel.updateTesteState(!isFilteredView)},
                pokemons = updatePokemon,
                onPokemonImageClick = {pokemon -> goToDetailActivity(pokemon)},
                onLoadMore = { pokemonsListViewModel.loadMorePokemons()},
                onUpdateFavoritesList = { pokemon ->
                    pokemonsListViewModel.updateFavoritesList(pokemon)
                },
                isFilteredView = isFilteredView,
                onStartBattle = {
                    Log.e("MAinActivity", "Pokemons Selecionados: $pokemon1 e $pokemon2")
                    goToBattleActivity(
                        pokemon1 = pokemon1,
                        pokemon2 = pokemon2
                    )
                },
                isSelected = { pokemon -> pokemonsListViewModel.onSelectPokemon.contains(pokemon) },
                onSelectPokemon = { pokemon ->
                    pokemonsListViewModel.selectPokemons(pokemon)
                }
            )
        }

    }
    private fun goToDetailActivity(pokemon: Pokemon) {
        val intent = Intent(this, PokemonDetailActivity::class.java)
        intent.putExtra("EXTRA_POKEMON_NAME", pokemon.name)
        startActivity(intent)
    }
    private fun goToBattleActivity(pokemon1: String, pokemon2: String) {
        val intent = Intent(this, BattleActivity::class.java)
        intent.putExtra("EXTRA_POKEMON_NAME_1", pokemon1)
        intent.putExtra("EXTRA_POKEMON_NAME_2", pokemon2)
        startActivity(intent)
    }

}