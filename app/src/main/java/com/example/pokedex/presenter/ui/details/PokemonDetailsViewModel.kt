package com.example.pokedex.presenter.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon

class PokemonDetailsViewModel(
    private var pokemonApiRepository: PokemonApiRepository
) : ViewModel(){
    var pokemonLiveData: MutableLiveData<Pokemon?> = MutableLiveData()

    suspend fun loadPokemon(name: String?) {
        if (name == null) {
            throw NullPointerException("Nome do Pokémon está vazio")
        }
        if (name.isEmpty()) {
            throw IllegalArgumentException("Nome do Pokémon está vazio")
        }
        val pokemon = pokemonApiRepository.getPokemons(name)
        pokemonLiveData.postValue(pokemon)
    }
}