package com.example.pokedex.data.network

import android.content.Context
import android.util.Log
import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.data.model.PokemonApiResult
import com.example.pokedex.data.model.PokemonListResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    // Instância do serviço da API
    private lateinit var service: PokemonApiService

    private var cache: Cache? = null

    fun initialize(context: Context) {
        val cacheSize = 20 * 1024 * 1024
        val cacheDirectory = File(context.cacheDir, "http_cache")
        cache = Cache(cacheDirectory, cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonApiService::class.java)
    }

    //chamadas à API

    // Método para listar os Pokémons
    fun listPokemons(limit: Int = 14, offset: Int = 0): PokemonListResponse? {
        Log.d("RetrofitClient", "Cache size before listPokemons(): ${cache?.size()} bytes")
        val call = service.listPokemons(limit, offset)
        Log.d("RetrofitClient", "Cache size after listPokemons(): ${cache?.size()} bytes")
        return call.execute().body()
    }

    // Método para obter os detalhes de um Pokémon específico
    fun getPokemon(name: String): PokemonApiResult? {
        Log.d("RetrofitClient", "Cache size before getPokemon($name): ${cache?.size()} bytes")
        val call = service.getPokemon(name)
        Log.d("RetrofitClient", "Cache size after getPokemon($name): ${cache?.size()} bytes")
        return call.execute().body()
    }
}