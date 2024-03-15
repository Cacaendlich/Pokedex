package com.example.pokedex.data.network

import com.example.pokedex.data.remote.PokemonApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    //https://pokeapi.co/api/v2/pokemon/?limit=100
    //https://pokeapi.co/api/v2/pokemon/
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private val service: PokemonApiService

    private val okHttpClient = OkHttpClient.Builder().build()

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(PokemonApiService::class.java)
    }
}