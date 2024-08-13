package com.example.pokedex.domain.model

import com.example.pokedex.data.model.Stats

//modelo pokemon list
data class Pokemon(
    val number: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,
    val stats: List<Stats>,
    val type: List<String> = emptyList(),
    var favorite: Boolean = false,
) {
    val imageUrl by lazy { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png" }

}
