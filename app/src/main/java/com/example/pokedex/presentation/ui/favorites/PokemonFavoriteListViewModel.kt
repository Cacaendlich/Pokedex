package com.example.pokedex.presentation.ui.favorites

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter

class PokemonFavoriteListViewModel : ViewModel() {
    private var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var pokemonsState = MutableLiveData<List<Pokemon?>>()


    fun loadPokemons(favoriteList: List<PokemonEntity>) {
        val limit = 100
        val offset = 0

        val pokemonsApiResultAPI = RetrofitClient.listPokemons(limit, offset)

        pokemonsApiResultAPI?.results?.let { results ->

            val pokesFiltrates = results.filter { pokemonItemResponse ->
                val name = pokemonItemResponse.name
                favoriteList.any{
                    it.name == name
                }
            }

            pokemonsState.postValue(pokesFiltrates.map { pokemonResult ->

                val name = pokemonResult.name

                val pokemonApiResult = RetrofitClient.getPokemon(name)

                pokemonApiResult?.let { Pokemon(pokemonApiResult.id, pokemonApiResult.name) }

            })

        }

    }

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

    fun removeFavorite(pokemon: Pokemon, context: Context, callback: () -> Unit) {
        Thread {
            deleteFavorite(pokemon.number, context) {
                loadFavorites(context) { favorites ->
                    loadPokemons(favorites)
                }
            }
        }.start()
    }

}