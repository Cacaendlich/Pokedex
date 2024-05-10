package com.example.pokedex.presentation.ui.pokemonsList


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter

class PokemonsListViewModel : ViewModel() {
    var pokemonsState = MutableLiveData<List<Pokemon?>>()

    var isLoading = MutableLiveData<Boolean>().apply { value = false }

    init {

        Thread {

            loadPokemons()

        }.start()

    }

    private fun loadPokemons() {

        val pokemonsApiResultAPI = RetrofitClient.listPokemons()

        pokemonsApiResultAPI?.results?.let {

            pokemonsState.postValue(it.map { pokemonResult ->

                val name = pokemonResult.name

                val pokemonApiResult = RetrofitClient.getPokemon(name)

                pokemonApiResult?.let { Pokemon(pokemonApiResult.id, pokemonApiResult.name) }

            })

        }

    }

    fun loadMorePokemons() {
        if (!isLoading.value!!) {
            isLoading.value = true

            val currentOffset = pokemonsState.value?.size ?: 0

            Thread {
                val pokemonsApiResultAPI = RetrofitClient.listPokemons(14, currentOffset)

                pokemonsApiResultAPI?.results?.let { newPokemons ->

                    val currentList = pokemonsState.value?.toMutableList() ?: mutableListOf()

                    currentList.addAll(newPokemons.mapNotNull { pokemonResult ->

                        val name = pokemonResult.name
                        val pokemonApiResult = RetrofitClient.getPokemon(name)
                        pokemonApiResult?.let { Pokemon(it.id, it.name) }

                    })

                    pokemonsState.postValue(currentList)

                }

                isLoading.postValue(false)

            }.start()

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

    fun addFavorites(pokemon: PokemonEntity, context: Context, callback: () -> Unit) {
        Thread{
            PokemonDataBase.getDataBase(context).PokemonDao().insertPokemonFavorite(pokemon)
            callback()
        }.start()
    }

    fun deleteFavorites(pokemon: PokemonEntity, context: Context, callback: () -> Unit) {
        Thread{
            PokemonDataBase.getDataBase(context).PokemonDao().deletePokemonFavorite(pokemon)
            callback()
        }.start()
    }

    fun updateFavoriteState(position: Int, adapter: PokemonAdapter) {
        val favoriteState = adapter.mPokemonList[position]?.favorite
        adapter.mPokemonList[position]?.favorite = favoriteState != true
        adapter.notifyItemChanged(position)
    }
}