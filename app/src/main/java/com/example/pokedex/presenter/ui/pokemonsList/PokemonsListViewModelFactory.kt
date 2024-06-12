package com.example.pokedex.presenter.ui.pokemonsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.data.repository.PokemonRepositoryImpl

//Fábrica para criar instâncias de PokemonsListViewModel
class PokemonsListViewModelFactory(private val pokemonRepositoryImpl: PokemonRepositoryImpl) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonsListViewModel::class.java)) {
            return PokemonsListViewModel(pokemonRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}