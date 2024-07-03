package com.example.pokedex.presenter.ui.favorites

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import com.example.pokedex.presenter.ui.pokemonsList.useCase.LoadPokemonsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonFavoriteListViewModel(
    private val loadPokemonsUseCase: LoadPokemonsUseCase
) : ViewModel() {
    private var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var favoriteList = MutableLiveData<List<PokemonEntity>>()

    fun loadPokemons(favoriteList: List<PokemonEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val limit = 1000
            val offset = 0
            val pokemonsApiResult = loadPokemonsUseCase.execute(limit, offset)

            pokemonsState.postValue(pokemonsApiResult.filter { pokemon ->
                favoriteList.any{
                    it.name == pokemon?.name
                }
            })
        }
    }

    fun loadFavorites(context: Context, callback: (List<PokemonEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val favorites = PokemonDataBase
                .getDataBase(context)
                .PokemonDao()
                .getAllPokemonsFavorites()
                .map { pokemonEntity ->
                    PokemonEntity(pokemonEntity.pokemonId, pokemonEntity.name)
                }
            isLoading.postValue(false)
            callback(favorites)
        }
    }

    private fun addFavorite(pokemon: PokemonEntity, context: Context, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            PokemonDataBase.getDataBase(context).PokemonDao().insertPokemonFavorite(pokemon)
            callback()
        }
    }

    private fun deleteFavorite(pokemonId: Int, context: Context, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO){
            PokemonDataBase.getDataBase(context).PokemonDao().deletePokemonFavorite(pokemonId)
            callback()
        }
    }

    fun isFavorite(favoriteList: List<PokemonEntity>, pokemon: Pokemon): Boolean {
        return favoriteList.any {
            it.name == pokemon.name
        }
    }

    fun updateFavoritesList(position: Int, pokemon: Pokemon, favoriteList: List<PokemonEntity>, adapter: PokemonAdapter, context: Context) {
        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        val isFavorite = isFavorite(favoriteList, pokemon)

        if (!isFavorite && !pokemon.favorite  || isFavorite && !pokemon.favorite) {
            addFavorite(pokemonFavorite, context) {
                adapter.updatePokemonFavoriteStatus(position, true)
            }
        } else {
            deleteFavorite(pokemon.number, context) {
                adapter.updatePokemonFavoriteStatus(position, false)
            }
        }
    }

    fun removeFavorite(pokemon: Pokemon, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavorite(pokemon.number, context) {
                loadFavorites(context) { favorites ->
                    loadPokemons(favorites)
                }
            }
        }
    }

}