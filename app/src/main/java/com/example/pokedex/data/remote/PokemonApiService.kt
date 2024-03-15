package com.example.pokedex.data.remote


import com.example.pokedex.domain.model.PokemonsApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int = 100): Call<PokemonsApiResult>
    @GET("pokemon/{id}")
    fun getPokemon(id: Int): Call<PokemonsApiResult>
}