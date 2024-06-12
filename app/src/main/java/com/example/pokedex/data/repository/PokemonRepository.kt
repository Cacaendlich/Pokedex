package com.example.pokedex.data.repository

import com.example.pokedex.domain.model.Pokemon

//operações de acesso aos dados relacionados aos Pokémon
interface PokemonRepository {
    suspend fun listPokemons(limit: Int, offset: Int): List<Pokemon?>
    suspend fun getPokemons(name: String): Pokemon?
}