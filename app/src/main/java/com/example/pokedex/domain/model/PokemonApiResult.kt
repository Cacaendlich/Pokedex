package com.example.pokedex.domain.model

data class PokemonsApiResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class PokemonResult(
    val name: String,
    val url: String
)

data class PokemonApiResult(
    val name: String,
)
