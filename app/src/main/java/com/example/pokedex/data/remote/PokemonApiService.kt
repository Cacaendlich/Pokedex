package com.example.pokedex.data.remote

import com.example.pokedex.domain.model.PokemonResponse
import retrofit2.http.GET

interface PokemonApiService {
    @GET("pokemon/random")
    suspend fun getRandomPokemon(): PokemonResponse
}