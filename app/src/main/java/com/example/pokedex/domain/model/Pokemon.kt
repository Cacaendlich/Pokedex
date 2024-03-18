package com.example.pokedex.domain.model

//modelo pokemon list
data class Pokemon(
    val imageUrl: String? = null,
    val number: Int? = null,
    val name: String,
    val url: String
)
