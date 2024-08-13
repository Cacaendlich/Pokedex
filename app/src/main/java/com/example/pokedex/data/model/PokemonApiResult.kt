package com.example.pokedex.data.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonItemResponse>
)

class PokemonItemResponse(
    val name: String,
)

data class PokemonApiResult(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int = 0,
    val weight: Int = 0,
    val stats : List<Stats>
)

data class Stats(
    val base_stat: Int,
    val stat: Stat
)
data class Stat(
    val name: String
)