package com.example.pokedex.data.remote

import com.example.pokedex.domain.model.PokemonListItem
import retrofit2.http.GET

interface PokemonApiService {
    @GET("pokemon-species")
    suspend fun getPokemons(): List<PokemonListItem>
}