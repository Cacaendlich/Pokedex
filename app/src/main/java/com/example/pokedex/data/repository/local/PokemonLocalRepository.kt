package com.example.pokedex.data.repository.local

import com.example.pokedex.data.local.model.PokemonEntity

interface PokemonLocalRepository {
    suspend fun getAllPokemons(): List<PokemonEntity?>
    suspend fun addFavorite(pokemon: PokemonEntity)
    suspend fun deleteFavorite(pokemonId: Int)
}