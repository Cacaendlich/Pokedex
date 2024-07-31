package com.example.pokedex.domain.model

data class PokemonDetail(
    val number: Int,
    val imageUrl: String,
    val name: String,
    val type: List<String> = emptyList(),
    val hp: Int = 0,
    val atk: Int = 0,
    val def: Int = 0,
    val spd: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0
)
