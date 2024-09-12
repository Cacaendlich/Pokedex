package com.example.pokedex.presenter.ui.battle

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BattleViewModel(
    private var pokemonApiRepository: PokemonApiRepository
): ViewModel() {
    private val _pokemon1StateFlow = MutableStateFlow<Pokemon?>(null)
    val pokemon1StateFlow: StateFlow<Pokemon?> = _pokemon1StateFlow

    private val _pokemon2StateFlow = MutableStateFlow<Pokemon?>(null)
    val pokemon2StateFlow: StateFlow<Pokemon?> = _pokemon2StateFlow

    suspend fun loadPokemon1(name: String) {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Nome do Pokémon está vazio")
        }
        val pokemon = pokemonApiRepository.getPokemons(name)
        _pokemon1StateFlow.value = pokemon
    }

    suspend fun loadPokemon2(name: String) {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Nome do Pokémon está vazio")
        }
        val pokemon = pokemonApiRepository.getPokemons(name)
        _pokemon2StateFlow.value = pokemon
    }

    fun validatePokemon(pokemon: Pokemon?) : Pokemon{
       if (pokemon == null){
           throw NullPointerException("Attempt to invoke method on a null object reference")
       }

       return pokemon
    }
}
