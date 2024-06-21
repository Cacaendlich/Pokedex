package com.example.pokedex.presenter.ui.pokemonsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.repository.PokemonRepository

class PokemonsListViewModelFactory(private val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonsListViewModel::class.java)) {
            return PokemonsListViewModel(pokemonRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}