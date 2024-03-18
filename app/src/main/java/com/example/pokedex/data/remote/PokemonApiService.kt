package com.example.pokedex.data.remote


import com.example.pokedex.domain.model.PokemonApiResult
import com.example.pokedex.domain.model.PokemonsApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int): Call<PokemonsApiResult>
    @GET("pokemon/{number}")
    fun getPokemon(number: Int): Call<PokemonApiResult>
}