package com.example.pokedex.presenter.ui.pokemonsList

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel

class RecyclerViewUtil(
    private val recyclerView: RecyclerView,
    private val favoriteListViewModel: PokemonFavoriteListViewModel,
    private val mfavoriteList: List<PokemonEntity>,
    private var pokemonAdapter: PokemonAdapter,
) {

    fun setupAdapter(onItemClickListener: PokemonAdapter.OnItemClickListener) {
        recyclerView.adapter = pokemonAdapter
        pokemonAdapter.setOnItemClickListener(onItemClickListener)
    }

    fun setupLayoutManager(layoutManager: GridLayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun updatePokemonFavorites(pokemons: List<Pokemon?>) {
        pokemons.forEach { pokemon ->
            pokemon?.let {
                it.favorite = favoriteListViewModel.isFavorite(mfavoriteList, it)
            }
        }
    }

    fun scrollToPosition(currentPosition: Int) {
        recyclerView.scrollToPosition(currentPosition)
    }

}