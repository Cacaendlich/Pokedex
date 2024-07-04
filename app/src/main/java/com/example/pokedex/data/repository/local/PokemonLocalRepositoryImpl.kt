package com.example.pokedex.data.repository.local

import android.content.Context
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity

class PokemonLocalRepositoryImpl(
    context: Context
) : PokemonLocalRepository {
    private val pokemonDao = PokemonDataBase.getDataBase(context).PokemonDao()
    override suspend fun getAllPokemons(): List<PokemonEntity> {
        return pokemonDao.getAllPokemonsFavorites()
    }

    override suspend fun addFavorite(pokemon: PokemonEntity) {
        pokemonDao.insertPokemonFavorite(pokemon)
    }

    override suspend fun deleteFavorite(pokemonId: Int) {
        pokemonDao.deletePokemonFavorite(pokemonId)
    }
}