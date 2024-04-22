package com.example.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.data.local.model.PokemonEntity

@Dao
interface PokemonDao {
    @Insert
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Delete
    suspend fun deletePokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM Pokemons_favorites")
    suspend fun getAllPokemons(): List<PokemonEntity>
}