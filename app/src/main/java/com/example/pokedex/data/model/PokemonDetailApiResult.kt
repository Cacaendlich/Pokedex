package com.example.pokedex.data.model

import java.sql.Types

data class PokemonDetailResponse(
    val height : Int,
    val weight : Int,
    val types : List<Types>
)




data class Types(
    val slot: Int,
    val type: List<Type>
)

data class Type(
    val name: String
)