package com.example.pokedex.domain.model

//modelo pokemon list
data class Pokemon(
    val number: Int,
    val name: String,
    var favorite: Boolean = false
) {
    val imageUrl by lazy { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png" }

}
