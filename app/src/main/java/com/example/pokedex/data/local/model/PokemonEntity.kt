package com.example.pokedex.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("PokeFavorites_table")
data class PokemonEntity(
    @PrimaryKey val pokemonId: Int
)
