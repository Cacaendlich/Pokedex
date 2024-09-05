package com.example.pokedex.presenter.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonFavoriteListViewModel(
    private var pokemonRepository: PokemonApiRepository,
    private var pokemonLocalRepository: PokemonLocalRepository
) : ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allPokemonsFavorites = pokemonLocalRepository.getAllPokemons()
                val favorites = allPokemonsFavorites
                    .map { pokemonEntity ->
                        PokemonEntity(pokemonEntity.pokemonId, pokemonEntity.name)
                    }

                favoriteList.postValue(favorites)
            } catch (e: Exception) {
                // Tratar exceção, se necessário
                favoriteList.postValue(emptyList())
            }
        }
    }

    fun loadAndFilterPokemonsFromFavoriteList(favoriteList: List<PokemonEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val limit = 1000
            val offset = 0

            val loadPokemons = pokemonRepository.listPokemons(limit, offset)
            val filteredPokemons = mutableListOf<Pokemon?>()

            for (pokemon in loadPokemons){
                if (favoriteList.any{ it.name == pokemon.name }){
                    pokemon.favorite = true
                    filteredPokemons.add(pokemon)
                    if (filteredPokemons.size == favoriteList.size){
                        break
                    }
                }
            }

            Log.e("PokemonFavoriteListViewModel", "$filteredPokemons")
            pokemonsState.postValue(filteredPokemons)

        }
    }

    private fun addFavorite(pokemon: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonLocalRepository.addFavorite(pokemon)
        }
    }

     private fun deleteFavorite(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO){
            pokemonLocalRepository.deleteFavorite(pokemonId)
        }
    }

    fun isFavorite(favoriteList: List<PokemonEntity>, pokemon: Pokemon): Boolean = favoriteList.any { it.name == pokemon.name }

    fun updateFavoritesList(pokemon: Pokemon, favoriteList: List<PokemonEntity>) {
        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        val isFavorite = favoriteList.any { it.name == pokemon.name }

        if (!isFavorite) {
            addFavorite(pokemonFavorite)
        } else {
            deleteFavorite(pokemon.number)
        }
    }

    fun removeFavorite(pokemon: Pokemon) = viewModelScope.launch(Dispatchers.IO) { deleteFavorite(pokemon.number) }

}