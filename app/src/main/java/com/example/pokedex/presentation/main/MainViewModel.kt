package com.example.pokedex.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon

class MainViewModel: ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()
    var isLoading = false


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

                pokemonApiResult?.let {
                    Pokemon(
                        pokemonApiResult.id,
                        pokemonApiResult.name
                    )
                }
            })

        }

    }

    fun loadMorePokemons() {
        if (!isLoading) {
            isLoading = true
            val currentOffset = pokemonsState.value?.size ?: 0
            Thread {
                val pokemonsApiResultAPI = RetrofitClient.listPokemons(limit = 10, offset = currentOffset)
                pokemonsApiResultAPI?.results?.let { newPokemons ->
                    val existingPokemons = pokemonsState.value?.toMutableList() ?: mutableListOf()
                    existingPokemons.addAll(newPokemons.mapNotNull { pokemonResult ->
                        val name = pokemonResult.name
                        val pokemonApiResult = RetrofitClient.getPokemon(name)
                        pokemonApiResult?.let { Pokemon(it.id, it.name) }
                    })
                    pokemonsState.postValue(existingPokemons)
                }
                isLoading = false
            }.start()
        }
    }




}