package com.example.pokedex.presenter.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel

class PokemonsListViewModelFactory(
    private val pokemonRepository: PokemonApiRepository,
    private val pokemonLocalRepository: PokemonLocalRepository
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(PokemonsListViewModel::class.java) -> {
                PokemonsListViewModel(pokemonRepository) as T
            }
            modelClass.isAssignableFrom(PokemonFavoriteListViewModel::class.java) -> {
                PokemonFavoriteListViewModel(pokemonRepository, pokemonLocalRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}