package com.example.pokedex.domain.model

//modelo pokemon list
data class Pokemon(
    val number: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,
    val type: List<String> = emptyList(),
    val hp: Int = 0,
    val atk: Int = 0,
    val def: Int = 0,
    val spd: Int = 0,
    var favorite: Boolean = false,
) {
    val imageUrl by lazy { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png" }

}
