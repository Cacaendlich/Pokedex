package com.example.pokedex.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon

class MainViewModel: ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    init {
        Thread(Runnable {
            loadPokemons()
        }).start()
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


}