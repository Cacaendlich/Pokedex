package com.example.pokedex.data.network

import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.domain.model.PokemonApiResult
import com.example.pokedex.domain.model.PokemonsApiResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    // Instância do serviço da API
    private val service: PokemonApiService
    // Configuração do cliente HTTP
    private val okHttpClient = OkHttpClient.Builder().build()

    init {
        // Configuração do Retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Criação do serviço da API utilizando o Retrofit
        service = retrofit.create(PokemonApiService::class.java)
    }

    //chamadas à API

    // Método para listar os Pokémons
    fun listPokemons(limit: Int = 40): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()
    }

    // Método para obter os detalhes de um Pokémon específico
    fun getPokemon(name: String): PokemonApiResult? {
        val call = service.getPokemon(name)

        return call.execute().body()
    }
}