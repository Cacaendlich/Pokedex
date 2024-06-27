package com.example.pokedex.presenter.ui.pokemonsList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.pokemonsList.useCase.LoadPokemonsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private val loadPokemonsUseCase: LoadPokemonsUseCase
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
            val pokemonsList = loadPokemonsUseCase.execute(LIMIT, OFFSET)
            pokemonsState.postValue(pokemonsList)

        }catch (e: Exception) {
            handleError(e)
        }

    }

    fun loadMorePokemons() {
        if (!isLoading.value!!) {
            setLoading(true)

            val currentOffset = pokemonsState.value?.size ?: 0

            viewModelScope.launch(Dispatchers.IO){
                try{
                    val newPokemonList = loadPokemonsUseCase.execute(LIMIT, currentOffset)
                    val currentList = pokemonsState.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(newPokemonList)
                    pokemonsState.postValue(currentList)
                } catch (e: Exception) {
                    handleError(e)
                } finally {
                    setLoading(false)
                }
            }
        }
    }
    fun refreshPokemons() {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO){
            try {
                loadPokemonsUseCase.execute(LIMIT, OFFSET)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                setLoading(false)
            }
        }
    }
     private fun setLoading(loading: Boolean) {
        isLoading.postValue(loading)
    }
    fun handleError(e: Exception) {
        Log.e("PokemonsListViewModel", "Error loading pokemons: ${e.message}")
    }
}