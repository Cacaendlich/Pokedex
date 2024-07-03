package com.example.pokedex.presenter.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel
import com.example.pokedex.presenter.ui.useCase.LoadPokemonsUseCase

class PokemonsListViewModelFactory(private val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val loadPokemonsUseCase = LoadPokemonsUseCase(pokemonRepository)

        return when {
            modelClass.isAssignableFrom(PokemonsListViewModel::class.java) -> {
                PokemonsListViewModel(loadPokemonsUseCase) as T
            }
            modelClass.isAssignableFrom(PokemonFavoriteListViewModel::class.java) -> {
                PokemonFavoriteListViewModel(loadPokemonsUseCase) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}