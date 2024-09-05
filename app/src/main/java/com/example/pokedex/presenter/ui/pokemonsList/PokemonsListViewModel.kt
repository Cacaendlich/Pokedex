package com.example.pokedex.presenter.ui.pokemonsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private var pokemonRepository: PokemonApiRepository
) : ViewModel() {
    companion object {
        private const val LIMIT = 20
        private const val OFFSET = 0
    }

    private val _pokemonsState = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonsState: StateFlow<List<Pokemon>> = _pokemonsState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadInitialPokemons()
        }
    }
    suspend fun loadInitialPokemons() {
        try {
            val pokemonsList = pokemonRepository.listPokemons(LIMIT, OFFSET)
            _pokemonsState.value = pokemonsList

        }catch (e: Exception) {
            handleError(e)
        }

    }

    fun loadMorePokemons() {
        val currentOffset = pokemonsState.value.size

        viewModelScope.launch(Dispatchers.IO){
            try{
                val pokemonList = pokemonRepository.listPokemons(LIMIT, currentOffset)
                _pokemonsState.value += pokemonList
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun refreshPokemons() {

        viewModelScope.launch(Dispatchers.IO){
            try {
                loadInitialPokemons()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception): Nothing {
        throw RuntimeException("Error loading pokemons: ${e.message}", e)
    }
}