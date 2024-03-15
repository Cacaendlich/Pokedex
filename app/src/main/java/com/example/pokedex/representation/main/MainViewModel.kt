package com.example.pokedex.representation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.domain.model.PokemonListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {
    private val pokemonListLiveData: MutableLiveData<List<PokemonListItem>> = MutableLiveData()
    private val pokemonApiService: PokemonApiService = RetrofitClient.retrofit.create(PokemonApiService::class.java)
    fun fetchPokemonList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pokemonApiService.getPokemons()
                pokemonListLiveData.postValue(response)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }

    }
}