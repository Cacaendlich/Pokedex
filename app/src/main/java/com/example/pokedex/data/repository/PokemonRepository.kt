package com.example.pokedex.data.repository

import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.model.PokemonEntity

class PokemonRepository(private val pokemonDao: PokemonDao) {

    suspend fun insertPokemonsFavorite(pokemonFavorite: PokemonEntity) {
        pokemonDao.insertPokemonFavorite(pokemonFavorite)
    }

    suspend fun deletePokemonsFavorite(pokemonFavorite: PokemonEntity) {
        pokemonDao.deletePokemonFavorite(pokemonFavorite)
    }

    suspend fun getAllPokemonsFavorites(): List<PokemonEntity> {
        return pokemonDao.getAllPokemonsFavorites()
    }

}