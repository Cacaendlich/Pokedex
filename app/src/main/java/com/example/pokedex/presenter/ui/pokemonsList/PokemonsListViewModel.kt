package com.example.pokedex.presenter.ui.pokemonsList

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    private val _pokemonAllState = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonsAllState: StateFlow<List<Pokemon>> = _pokemonAllState

    private val _pokemonFavoriteState = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonsFavoriteState: StateFlow<List<Pokemon>> = _pokemonFavoriteState

    private val _isDisplayingFavorites = MutableStateFlow(false)
    val isDisplayingFavorites: StateFlow<Boolean> = _isDisplayingFavorites

    private val _onSelectPokemon =  mutableStateListOf<Pokemon>()
    val onSelectPokemon: List<Pokemon> get() = _onSelectPokemon

    private val _pokemon1State = MutableStateFlow("")
    val pokemon1State: StateFlow<String> = _pokemon1State

    private val _pokemon2State = MutableStateFlow("")
    val pokemon2State: StateFlow<String> = _pokemon2State

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

    private fun handleError(e: Exception): Nothing {
        throw RuntimeException("Error loading pokemons: ${e.message}", e)
    }


    // favorites metode



    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allPokemonsFavorites = pokemonLocalRepository.getAllPokemons()
                favoriteList.postValue(allPokemonsFavorites)
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

            _pokemonFavoriteState.value = filteredPokemons
        }
    }

    fun toggleFavoritesView(value: Boolean) {
        _isDisplayingFavorites.value = value
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

    private fun updateSelectedPokemons(){
        val selectPokemons = _onSelectPokemon
        _pokemon1State.value = selectPokemons[0].name
        _pokemon2State.value = selectPokemons[1].name
    }


    fun selectPokemons(pokemon: Pokemon){
        if (_onSelectPokemon.contains(pokemon)) {
            _onSelectPokemon.remove(pokemon)
        } else if (_onSelectPokemon.size < 2) {
            _onSelectPokemon.add(pokemon)

        }else {
            Log.e("PokemonListViewModel", "Limite da lista excedido ${pokemon.name} nao pode ser adicionado!")
        }

        if (_onSelectPokemon.size == 2){ updateSelectedPokemons() }
    }

    fun removeAllBattle(){
        _onSelectPokemon.removeAll(onSelectPokemon)
    }

    fun showPokemonSelectionTips(context: Context){
        when(_onSelectPokemon.size) {
            0 -> {
                Toast.makeText(context, "Select two Pokémon to BATTLE!", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                Toast.makeText(context, "Select the second Pokémon to BATTLE!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                updateSelectedPokemons()
            }
        }
    }

    fun showStartBattleTips(context: Context){
        when(_onSelectPokemon.size) {
            1 -> {
                Toast.makeText(context, "Select the second Pokémon to BATTLE!", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                Toast.makeText(context, "Click to BATTLE!", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }





}