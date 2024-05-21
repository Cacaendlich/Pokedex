package com.example.pokedex.presentation.ui.favorites

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter

class PokemonFavoriteListViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>().apply { value = false }

    fun loadFavorites(context: Context, callback: (List<PokemonEntity>) -> Unit) {
        Thread{
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
        }.start()
    }

    private fun addFavorite(pokemon: PokemonEntity, context: Context, callback: () -> Unit) {
        Thread{
            PokemonDataBase.getDataBase(context).PokemonDao().insertPokemonFavorite(pokemon)
            callback()
        }.start()
    }

    private fun deleteFavorite(pokemonId: Int, context: Context, callback: () -> Unit) {
        Thread{
            PokemonDataBase.getDataBase(context).PokemonDao().deletePokemonFavorite(pokemonId)
            callback()
        }.start()
    }

    fun isFavorite(favoriteList: List<PokemonEntity>, pokemon: Pokemon): Boolean {
        return favoriteList.any {
            it.name == pokemon.name
        }
    }

    fun updateFavoritesList(position: Int, pokemon: Pokemon, favoriteList: List<PokemonEntity>, adapter: PokemonAdapter, context: Context) {
        val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

        val isFavorite = isFavorite(favoriteList, pokemon)
        Log.d("PokemonsListFragment", "${pokemon.name} - Está na lista de favoritos: $isFavorite")

        if (!isFavorite && !pokemon.favorite  || isFavorite && !pokemon.favorite) {
            addFavorite(pokemonFavorite, context) {
                Log.d("PokemonsListFragment", "${pokemon.name} - Add aos favoritos com SUCESSO!")
                adapter.updatePokemonFavoriteStatus(position, true)
            }
        } else {
            deleteFavorite(pokemon.number, context) {
                Log.d("PokemonsListFragment", "${pokemon.name} - Delete dos favoritos com SUCESSO!")
                adapter.updatePokemonFavoriteStatus(position, false)
            }
        }
    }
}