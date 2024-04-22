package com.example.pokedex.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Pokemons_favorites")
data class PokemonEntity(
    @PrimaryKey val pokemonId: Int,
    @ColumnInfo val name: String
)
