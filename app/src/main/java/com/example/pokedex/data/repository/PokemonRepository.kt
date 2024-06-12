package com.example.pokedex.data.repository

import com.example.pokedex.domain.model.Pokemon

interface PokemonRepository {
    suspend fun listPokemons(limit: Int, offset: Int): List<Pokemon?>
    suspend fun getPokemons(name: String): Pokemon?
}