package com.example.pokedex.presentation.ui.pokemonsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon

class PokemonsListViewModel : ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var isLoading = MutableLiveData<Boolean>().apply { value = false }

    init {

        Thread {

            loadPokemons()

        }.start()

    }

    private fun loadPokemons() {

        val pokemonsApiResultAPI = RetrofitClient.listPokemons()

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

            Thread {
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

            }.start()

        }
    }
}