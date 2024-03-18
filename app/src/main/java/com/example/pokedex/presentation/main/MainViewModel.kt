package com.example.pokedex.presentation.main

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
//    private var pokemons = MutableLiveData<List<Pokemon?>>()
//
//    init {
//        Thread(Runnable {
//            loadPokemons()
//        }).start()
//    }
    private fun loadPokemons() {
//        val pokemonsApiResult = RetrofitClient.listPokemons()
//
//        pokemonsApiResult?.results?.let {
//            pokemons.postValue(it.map { pokemonResult ->
//                val id = pokemonResult.url
//                    .replace("https://pokeapi.co/api/v2/pokemon/", "")
//                    .replace("/", "").toInt()
//
//                val pokemonApiResult = RetrofitClient.getPokemon(id)
//
//                pokemonApiResult?.let {
//                    Pokemon(
//                        pokemonApiResult.id,
//                        pokemonApiResult.name
//                    )
//                }
//            })
//        }
    }
}