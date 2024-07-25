package com.example.pokedex.presenter.ui.favorites

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
    private var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)

            try {
                val pokemonEntities = pokemonLocalRepository.getAllPokemons()
                val favorites = pokemonEntities.filterNotNull()
                    .map { pokemonEntity ->
                        PokemonEntity(pokemonEntity.pokemonId, pokemonEntity.name)
                    }

                isLoading.postValue(false)
                favoriteList.postValue(favorites)
            } catch (e: Exception) {
                // Tratar exceção, se necessário
                isLoading.postValue(false)
                favoriteList.postValue(emptyList())
            }
        }
    }

    fun loadAndFilterPokemonsFromFavoriteList(favoriteList: List<PokemonEntity>) {
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

    private fun addFavorite(pokemon: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonLocalRepository.addFavorite(pokemon)
            loadFavorites()
        }
    }

     private fun deleteFavorite(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO){
            pokemonLocalRepository.deleteFavorite(pokemonId)
            loadFavorites()
        }
    }

    fun isFavorite(favoriteList: List<PokemonEntity>, pokemon: Pokemon): Boolean = favoriteList.any { it.name == pokemon.name }

    fun updateFavoritesList(pokemon: Pokemon, favoriteList: List<PokemonEntity>) {
        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        val isFavorite = isFavorite(favoriteList, pokemon)

        if (!isFavorite && !pokemon.favorite  || isFavorite && !pokemon.favorite) {
            addFavorite(pokemonFavorite)
        } else {
            deleteFavorite(pokemon.number)
        }
    }

    fun removeFavorite(pokemon: Pokemon) = viewModelScope.launch(Dispatchers.IO) { deleteFavorite(pokemon.number) }

}