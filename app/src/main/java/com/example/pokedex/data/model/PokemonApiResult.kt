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

)
