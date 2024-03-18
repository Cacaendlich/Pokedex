package com.example.pokedex.data.network

import android.util.Log
import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.domain.model.PokemonApiResult
import com.example.pokedex.domain.model.PokemonsApiResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    fun listPokemons(limit: Int = 100): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()

//        call.enqueue(object : Callback<PokemonsApiResult> {
//            override fun onResponse(
//                call: Call<PokemonsApiResult>,
//                response: Response<PokemonsApiResult>
//            ) {
//                if(response.isSuccessful) {
//                    val body = response.body()
//                    body?.results?.let {
//                        Log.e("POKEMON_API", it[4].name)
//                    }
//                }
//                Log.e("POKEMON_API", "Pokemons list load.")
//            }
//
//            override fun onFailure(call: Call<PokemonsApiResult>, t: Throwable) {
//                Log.e("POKEMON_API", "Error load pokemons list.", t)
//            }
//
//        })
    }
//    fun listPokemons(limit: Int = 100): PokemonsApiResult? {
//        val call = service.listPokemons(limit)
//
//        return call.execute().body()
//    }
//
//    fun getPokemon(id: Int): PokemonApiResult? {
//        val call = service.getPokemon(id)
//
//        return call.execute().body()
//    }
}