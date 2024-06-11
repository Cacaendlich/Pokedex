package com.example.pokedex.presentation.ui.pokemonsList


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonsListViewModel : ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var isLoading = MutableLiveData<Boolean>().apply { value = false }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadPokemons()
        }
    }

    private fun loadPokemons() {
        val limit = 14
        val offset = 0

        val pokemonsApiResultAPI = RetrofitClient.listPokemons(limit, offset)

        pokemonsApiResultAPI?.results?.let {

            pokemonsState.postValue(it.map { pokemonResult ->

                val name = pokemonResult.name

                val pokemonApiResult = RetrofitClient.getPokemon(name)

                pokemonApiResult?.let { Pokemon(pokemonApiResult.id, pokemonApiResult.name) }

            })

        }

    }

    fun loadMorePokemons() {
        if (!isLoading.value!!) {
            isLoading.value = true

            val currentOffset = pokemonsState.value?.size ?: 0

            viewModelScope.launch(Dispatchers.IO){
                val pokemonsApiResultAPI = RetrofitClient.listPokemons(14, currentOffset)

                pokemonsApiResultAPI?.results?.let { newPokemons ->

                    val currentList = pokemonsState.value?.toMutableList() ?: mutableListOf()

                    currentList.addAll(newPokemons.mapNotNull { pokemonResult ->

                        val name = pokemonResult.name
                        val pokemonApiResult = RetrofitClient.getPokemon(name)
                        pokemonApiResult?.let { Pokemon(it.id, it.name) }

                    })

                    pokemonsState.postValue(currentList)

                }

                isLoading.postValue(false)
            }
        }
    }

    fun refreshPokemons() {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO){
            try {
                loadPokemons()
            } catch (e: Exception) {
                Log.e("PokemonsListViewModel", "Erro ao recarregar os pokémons: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

}