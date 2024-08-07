package com.example.pokedex.presenter.ui.pokemonsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private var pokemonRepository: PokemonApiRepository
) : ViewModel() {
    companion object {
        private const val LIMIT = 14
        private const val OFFSET = 0
    }

    var pokemonsState = MutableLiveData<List<Pokemon?>>()
    var isLoading = MutableLiveData<Boolean>().apply { value = false }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadInitialPokemons()
        }
    }
    suspend fun loadInitialPokemons() {
        try {
            val pokemonsList = pokemonRepository.listPokemons(LIMIT, OFFSET)
            pokemonsState.postValue(pokemonsList)

        }catch (e: Exception) {
            handleError(e)
        }

    }

    fun loadMorePokemons() {
        setLoading(true)

        val currentOffset = pokemonsState.value?.size ?: 0

        viewModelScope.launch(Dispatchers.IO){
            try{
                val pokemonList = pokemonRepository.listPokemons(LIMIT, currentOffset)
                val updatedList = pokemonsState.value?.toMutableList() ?: mutableListOf()
                updatedList.addAll(pokemonList)
                pokemonsState.postValue(updatedList)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                setLoading(false)
            }
        }
    }
    fun refreshPokemons() {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO){
            try {
                loadInitialPokemons()
            } catch (e: Exception) {
                handleError(e)
            } finally {
                setLoading(false)
            }
        }
    }
    private fun setLoading(loading: Boolean) = isLoading.postValue(loading)

    private fun handleError(e: Exception): Nothing {
        throw RuntimeException("Error loading pokemons: ${e.message}", e)
    }
}