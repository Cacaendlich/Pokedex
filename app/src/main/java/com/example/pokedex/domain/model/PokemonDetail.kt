package com.example.pokedex.domain.model

data class PokemonDetail(
    val number: Int,
    val imageUrl: String,
    val name: String,
    val type: List<String>,
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val xp: Int
)
