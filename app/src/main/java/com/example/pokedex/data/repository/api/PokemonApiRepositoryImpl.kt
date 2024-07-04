package com.example.pokedex.data.repository.api

import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon

class PokemonApiRepositoryImpl(private val retrofitClient: RetrofitClient) : PokemonApiRepository {
    override suspend fun listPokemons(limit: Int, offset: Int): List<Pokemon?> {
        val pokemonsApiResult = retrofitClient.listPokemons(limit, offset)

        return pokemonsApiResult?.results?.map {pokemonItemResponse ->
            val name = pokemonItemResponse.name
            getPokemons(name)
        } ?: emptyList()
    }

    override suspend fun getPokemons(name: String): Pokemon? {
        val pokemonItemResult = retrofitClient.getPokemon(name)
        return pokemonItemResult?.let {
            Pokemon(it.id, it.name)
        }
    }

}