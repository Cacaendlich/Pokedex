package com.example.pokedex.presenter.ui.favorites

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import com.example.pokedex.presenter.ui.details.PokemonDetailActivity

class PokemonFavoriteListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var favoriteListViewModel: PokemonFavoriteListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var mfavoriteList: List<PokemonEntity>

    companion object {
        fun newInstance() = PokemonFavoriteListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonsListBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListViewModel = ViewModelProvider(requireActivity())[PokemonFavoriteListViewModel::class.java]

        favoriteListViewModel.loadFavorites(requireContext()) { favorites ->
            mfavoriteList = favorites
            favoriteListViewModel.favoriteList.postValue(mfavoriteList)
            Log.d("PokemonsFavoriteListFragment", "A lista de favoritos foi atualizada para: ${favoriteListViewModel.favoriteList.value}")
            favoriteListViewModel.loadPokemons(mfavoriteList)
        }

        mfavoriteList = favoriteListViewModel.favoriteList.value ?: emptyList()


        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true)

        favoriteListViewModel.pokemonsState.observe(requireActivity()) { pokemons ->
            pokemons?.let {
                updateRecyclerView(pokemons)
                progressBar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

    }

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
        if (!isAdded) {
            return
        }

        pokemons.forEach { pokemon ->
            pokemon?.let {
                it.favorite = favoriteListViewModel.isFavorite(mfavoriteList, it)
            }
        }

        mLayoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(requireActivity(), 3)
            } else {
                GridLayoutManager(requireActivity(), 2)
            }
        mPokemonAdapter = PokemonAdapter(pokemons)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mPokemonAdapter

        mPokemonAdapter.setOnItemClickListener(this)
    }

    override fun onFavoriteClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            favoriteListViewModel.removeFavorite( it, requireContext())
        }
    }

    override fun onDetailClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
            startActivity(intent)
        }
    }

}