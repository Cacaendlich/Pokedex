package com.example.pokedex.presenter.ui.pokemonsList

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private var pokemonRepository: PokemonApiRepository,
    private var pokemonLocalRepository: PokemonLocalRepository

) : ViewModel() {
    companion object {
        private const val LIMIT = 20
        private const val OFFSET = 0
    }
    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    var isSelected : Boolean = false

    private val _pokemonAllState = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonsAllState: StateFlow<List<Pokemon>> = _pokemonAllState

    private val _pokemonFavoriteState = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonsFavoriteState: StateFlow<List<Pokemon>> = _pokemonFavoriteState

    private val _isFilteredView = MutableStateFlow(false)
    val isFilteredView: StateFlow<Boolean> = _isFilteredView

    fun updateTesteState(value: Boolean) {
        _isFilteredView.value = value
    }

    private val _onSelectPokemon =  mutableStateListOf<Pokemon>()
    val onSelectPokemon: List<Pokemon> get() = _onSelectPokemon

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadInitialPokemons()
        }
    }
    suspend fun loadInitialPokemons() {
        try {
            val pokemonsList = pokemonRepository.listPokemons(LIMIT, OFFSET)
            _pokemonAllState.value = pokemonsList

        }catch (e: Exception) {
            handleError(e)
        }

    }

    fun loadMorePokemons() {
        val currentOffset = pokemonsAllState.value.size

        viewModelScope.launch(Dispatchers.IO){
            try{
                val pokemonList = pokemonRepository.listPokemons(LIMIT, currentOffset)
                _pokemonAllState.value += pokemonList
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun refreshPokemons() {

        viewModelScope.launch(Dispatchers.IO){
            try {
                loadInitialPokemons()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception): Nothing {
        throw RuntimeException("Error loading pokemons: ${e.message}", e)
    }


    // favorites metode



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
            val filteredPokemons = mutableListOf<Pokemon>()

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

            _pokemonFavoriteState.value = filteredPokemons
        }
    }

    fun updateFavoritesList(pokemon: Pokemon) {
        // Atualiza instantaneamente o estado do favorito na lista de Pokémons
        val updatedPokemons = _pokemonAllState.value.map {
            if (it.name == pokemon.name) {
                it.copy(favorite = !it.favorite) // Alterna o status de favorito
            } else {
                it
            }
        }

        // Atualiza o estado com a lista modificada para refletir na UI imediatamente
        _pokemonAllState.value = updatedPokemons

        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        viewModelScope.launch(Dispatchers.IO) {
            if (!pokemon.favorite) {
                pokemonLocalRepository.addFavorite(pokemonFavorite)
            } else {
                pokemonLocalRepository.deleteFavorite(pokemon.number)
            }

            loadFavorites()
        }
    }

    //Battle

    fun selectPokemons(pokemon: Pokemon){
        if (_onSelectPokemon.contains(pokemon)) {
            _onSelectPokemon.remove(pokemon)
            Log.e("MinaActivity", "${pokemon.name} removido")
        } else if (_onSelectPokemon.size < 2) {
            _onSelectPokemon.add(pokemon)
            Log.e("MinaActivity", "${pokemon.name} adicionado")

        }else {
            Log.e("PokemonListViewModel", "Limite da lista excedido ${pokemon.name} nao pode ser adicionado!")
        }
        Log.e("PokemonListViewModel", "Pokemons Selecionados: ${_onSelectPokemon.map { it.name }}")
        Log.e("PokemonListViewModel", "Pokemons Selecionados tamanho: ${_onSelectPokemon.size}")
    }


}