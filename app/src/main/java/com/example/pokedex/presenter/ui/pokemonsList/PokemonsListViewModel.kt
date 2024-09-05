package com.example.pokedex.presenter.ui.pokemonsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private var pokemonRepository: PokemonApiRepository,
    private var pokemonLocalRepository: PokemonLocalRepository

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


    // favorites metode


    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allPokemonsFavorites = pokemonLocalRepository.getAllPokemons()
                val favorites = allPokemonsFavorites
                    .map { pokemonEntity ->
                        PokemonEntity(pokemonEntity.pokemonId, pokemonEntity.name)
                    }

                favoriteList.postValue(favorites)
            } catch (e: Exception) {
                // Tratar exceção, se necessário
                favoriteList.postValue(emptyList())
            }
        }
    }

    fun updateFavoritesList(pokemon: Pokemon) {
        // Atualiza instantaneamente o estado do favorito na lista de Pokémons
        val updatedPokemons = _pokemonsState.value.map {
            if (it.name == pokemon.name) {
                it.copy(favorite = !it.favorite) // Alterna o status de favorito
            } else {
                it
            }
        }

        // Atualiza o estado com a lista modificada para refletir na UI imediatamente
        _pokemonsState.value = updatedPokemons

        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        viewModelScope.launch(Dispatchers.IO) {
            if (!pokemon.favorite) {
                pokemonLocalRepository.addFavorite(pokemonFavorite)
            } else {
                pokemonLocalRepository.deleteFavorite(pokemon.number)
            }

            loadFavorites()
        }
    }


}