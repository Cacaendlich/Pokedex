package com.example.pokedex.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon

class MainViewModel: ViewModel() {
    var pokemons = MutableLiveData<List<Pokemon?>>()

    init {
        Thread(Runnable {
            loadPokemons()
        }).start()
    }

    private fun loadPokemons() {
        val pokemonsApiResult = RetrofitClient.listPokemons()

        pokemonsApiResult?.results?.let {


            pokemons.postValue(it.map { pokemonResult ->
                val number = pokemonResult.url
                    .removePrefix("https://pokeapi.co/api/v2/pokemon/")
                    .removeSuffix("/").toInt()

                val pokemonApiResult = RetrofitClient.getPokemon(number)

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