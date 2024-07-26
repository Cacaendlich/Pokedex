package com.example.pokedex.data.repository.api

import com.example.pokedex.domain.model.Pokemon

interface PokemonApiRepository {
    suspend fun listPokemons(limit: Int, offset: Int): List<Pokemon?>
    suspend fun getPokemons(name: String): Pokemon?
}