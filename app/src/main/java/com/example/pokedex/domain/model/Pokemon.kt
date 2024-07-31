package com.example.pokedex.domain.model

//modelo pokemon list
data class Pokemon(
    val number: Int,
    val name: String,
    var favorite: Boolean = false,
    val type: List<String> = emptyList(),
    val hp: Int = 0,
    val atk: Int = 0,
    val def: Int = 0,
    val spd: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0
) {
    val imageUrl by lazy { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png" }

}
