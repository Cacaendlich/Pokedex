package com.example.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.data.local.model.PokemonEntity

@Dao
interface PokemonDao {
    @Insert
    fun insertPokemonFavorite(pokemon: PokemonEntity)

    @Query("DELETE FROM PokeFavorites_table WHERE pokemonId = :pokemonId")
    fun deletePokemonFavorite(pokemonId: Int)

    @Query("SELECT * FROM PokeFavorites_table")
    fun getAllPokemonsFavorites(): List<PokemonEntity>
}