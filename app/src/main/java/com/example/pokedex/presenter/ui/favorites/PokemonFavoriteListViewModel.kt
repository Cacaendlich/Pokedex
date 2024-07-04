package com.example.pokedex.presenter.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonFavoriteListViewModel(
    private var pokemonRepository: PokemonApiRepository,
    private var pokemonLocalRepository: PokemonLocalRepository
) : ViewModel() {
    private var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    fun loadPokemons(favoriteList: List<PokemonEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val limit = 1000
            val offset = 0

            val loadPokemons = pokemonRepository.listPokemons(limit, offset)

            pokemonsState.postValue(loadPokemons.filter { pokemon ->
                favoriteList.any{
                    it.name == pokemon?.name
                }
            })
        }
    }

    fun loadFavorites(callback: (List<PokemonEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val favorites = pokemonLocalRepository.getAllPokemons()
                .map { pokemonEntity ->
                    PokemonEntity(pokemonEntity!!.pokemonId, pokemonEntity.name)
                }
            isLoading.postValue(false)
            callback(favorites)
        }
    }

    private fun addFavorite(pokemon: PokemonEntity, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonLocalRepository.addFavorite(pokemon)
            callback()
        }
    }

    private fun deleteFavorite(pokemonId: Int, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO){
            pokemonLocalRepository.deleteFavorite(pokemonId)
            callback()
        }
    }

    fun isFavorite(favoriteList: List<PokemonEntity>, pokemon: Pokemon): Boolean {
        return favoriteList.any {
            it.name == pokemon.name
        }
    }

    fun updateFavoritesList(position: Int, pokemon: Pokemon, favoriteList: List<PokemonEntity>, adapter: PokemonAdapter) {
        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        val isFavorite = isFavorite(favoriteList, pokemon)

        if (!isFavorite && !pokemon.favorite  || isFavorite && !pokemon.favorite) {
            addFavorite(pokemonFavorite) {
                adapter.updatePokemonFavoriteStatus(position, true)
            }
        } else {
            deleteFavorite(pokemon.number) {
                adapter.updatePokemonFavoriteStatus(position, false)
            }
        }
    }

    fun removeFavorite(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavorite(pokemon.number) {
                loadFavorites { favorites ->
                    loadPokemons(favorites)
                }
            }
        }
    }

}