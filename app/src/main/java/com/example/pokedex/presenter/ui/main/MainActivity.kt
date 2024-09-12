package com.example.pokedex.presenter.ui.main

import android.content.Intent
import android.os.Bundle
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
import com.example.pokedex.presenter.ui.factory.ViewModelFactory
import com.example.pokedex.presenter.ui.main.view.MainScreen
import com.example.pokedex.presenter.ui.pokemonsList.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mFavoriteList: List<PokemonEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(this)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = ViewModelFactory(pokemonApiRepository, pokemonLocalRepository)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.loadFavorites()

        mainViewModel.favoriteList.observe(this) { favoriteList ->
            mFavoriteList = favoriteList
            mainViewModel.loadAndFilterPokemonsFromFavoriteList(mFavoriteList)
        }


        setContent {
            val pokemons by mainViewModel.pokemonsAllState.collectAsState()

            val favoritePokemons by mainViewModel.pokemonsFavoriteState.collectAsState()

            val isDisplayingFavorites by mainViewModel.isDisplayingFavorites.collectAsState()

            val pokemon1 by mainViewModel.pokemon1State.collectAsState()
            val pokemon2 by mainViewModel.pokemon2State.collectAsState()


            val updatePokemon = if (!isDisplayingFavorites) {
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
                onFavoriteList = { mainViewModel.toggleFavoritesView(!isDisplayingFavorites)},
                pokemons = updatePokemon,
                onPokemonImageClick = {pokemon -> goToDetailActivity(pokemon)},
                onLoadMore = { mainViewModel.loadMorePokemons()},
                onUpdateFavoritesList = { pokemon ->
                    mainViewModel.updateFavoritesList(pokemon)
                },
                isFilteredView = isDisplayingFavorites,
                onStartBattle = {
                    if (mainViewModel.onSelectPokemon.size == 2){
                        goToBattleActivity(
                            pokemon1 = pokemon1,
                            pokemon2 = pokemon2
                        )
                    }
                    mainViewModel.showPokemonSelectionTips(this)
                },
                isSelected = { pokemon -> mainViewModel.onSelectPokemon.contains(pokemon) },
                onSelectPokemon = { pokemon ->
                    mainViewModel.selectPokemons(pokemon)
                    mainViewModel.showStartBattleTips(this)
                }
            )
        }

    }

    override fun onResume() {
        super.onResume()
        mainViewModel.removeAllBattle()
        //onResume é invocado sempre que a Activity volta a ser exibida após estar em segundo plano
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