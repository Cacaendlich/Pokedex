package com.example.pokedex.domain.model

// Representa o resultado da lista de Pokémons obtidos da API
data class PokemonsApiResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)

// Representa um resultado individual de Pokémon na lista.
data class PokemonResult(
    val name: String,
    val url: String,
)

// Representa os dados necessários de um Pokémon, incluindo a URL da imagem.
data class PokemonApiResult(
    val imageUrl: String,
    val name: String,
)
