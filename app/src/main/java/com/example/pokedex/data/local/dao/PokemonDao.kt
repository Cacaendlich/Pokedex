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

    @Delete
    suspend fun deletePokemonFavorite(pokemon: PokemonEntity)

    @Query("SELECT * FROM PokeFavorites_table")
    fun getAllPokemonsFavorites(): List<PokemonEntity>
}