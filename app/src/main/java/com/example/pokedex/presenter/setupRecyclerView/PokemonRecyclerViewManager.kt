package com.example.pokedex.presenter.setupRecyclerView

import android.content.res.Configuration
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import com.example.pokedex.presenter.layout.LandscapeLayoutManagerProvider
import com.example.pokedex.presenter.layout.PortraitLayoutManagerProvider
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel

class PokemonRecyclerViewManager(
    private  var mFragment: Fragment,
    private  var mRecyclerView: RecyclerView,
    private  var mLayoutManager: GridLayoutManager,
    private  var mPokemonAdapter: PokemonAdapter,
    private  var favoriteListViewModel: PokemonFavoriteListViewModel,
    private  var mfavoriteList: List<PokemonEntity>
) : PokemonAdapter.OnItemClickListener{
    private var currentPosition = 0

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
        if (!mFragment.isAdded) {
            return
        }
        updatePokemonFavorites(pokemons)
        setupLayoutManager()
        setupAdapter(pokemons)
        scrollToPosition(currentPosition)
    }

    private fun setupAdapter(pokemons: List<Pokemon?>) {
        mPokemonAdapter = PokemonAdapter(pokemons)
        mRecyclerView.adapter = mPokemonAdapter
        mPokemonAdapter.setOnItemClickListener(this)
    }

    private fun setupLayoutManager() {
        val layoutManagerProvider = if (mFragment.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeLayoutManagerProvider(mFragment.requireActivity())
        } else {
            PortraitLayoutManagerProvider(mFragment.requireActivity())
        }

        mLayoutManager = layoutManagerProvider.getLayoutManager()
        mRecyclerView.layoutManager = mLayoutManager

    }

    private fun updatePokemonFavorites(pokemons: List<Pokemon?>){
        pokemons.forEach { pokemon ->
            pokemon?.let {
                it.favorite = favoriteListViewModel.isFavorite(mfavoriteList, it)
            }
        }
    }

    private fun scrollToPosition(currentPosition: Int){
        mRecyclerView.scrollToPosition(currentPosition)
    }

    override fun onFavoriteClick(position: Int, imageView: ImageView) {
        TODO("Not yet implemented")
    }

    override fun onDetailClick(position: Int, imageView: ImageView) {
        TODO("Not yet implemented")
    }
}