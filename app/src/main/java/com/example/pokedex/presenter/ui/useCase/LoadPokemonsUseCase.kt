package com.example.pokedex.presenter.ui.useCase

import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.domain.model.Pokemon

class LoadPokemonsUseCase(
    private var pokemonRepository: PokemonRepository
) {
    suspend fun execute(limit: Int, offset: Int): List<Pokemon?> {
        return pokemonRepository.listPokemons(limit, offset)
    }
}
