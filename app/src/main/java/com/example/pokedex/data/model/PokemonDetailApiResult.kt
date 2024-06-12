package com.example.pokedex.data.model

import java.sql.Types

data class PokemonDetailResponse(
    val height : Int,
    val weight : Int,
    val stats : List<Stats>,
    val types : List<Types>
)

data class Stats(
    val base_stat: Int,
    val stat: List<Stat>
)

data class Stat(
    val name: String
)

data class Types(
    val slot: Int,
    val type: List<Type>
)

data class Type(
    val name: String
)