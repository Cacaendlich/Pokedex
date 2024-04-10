package com.example.pokedex.data.remote


import com.example.pokedex.data.model.PokemonApiResult
import com.example.pokedex.data.model.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokemonListResponse>
    @GET("pokemon/{name}")
    fun getPokemon(@Path("name")name: String): Call<PokemonApiResult>
}