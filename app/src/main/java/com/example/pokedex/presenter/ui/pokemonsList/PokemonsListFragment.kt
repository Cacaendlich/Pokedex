package com.example.pokedex.presenter.ui.pokemonsList

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
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonAdapter
import com.example.pokedex.presenter.ui.details.PokemonDetailActivity
import com.example.pokedex.presenter.ui.factory.PokemonsListViewModelFactory
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListViewModel

class PokemonsListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var pokemonsListViewModel: PokemonsListViewModel
    private lateinit var favoriteListViewModel: PokemonFavoriteListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var mfavoriteList: List<PokemonEntity>

    private var currentPosition = 0

    companion object {
        fun newInstance() = PokemonsListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonsListBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(requireActivity())
        val factory = PokemonsListViewModelFactory(pokemonApiRepository, pokemonLocalRepository)

        pokemonsListViewModel = ViewModelProvider(requireActivity(), factory)[PokemonsListViewModel::class.java]
        favoriteListViewModel = ViewModelProvider(requireActivity(), factory)[PokemonFavoriteListViewModel::class.java]

        favoriteListViewModel.favoriteList.observe(viewLifecycleOwner) { favorites ->
            mfavoriteList = favorites
            Log.d(
                "PokemonsFavoriteListFragment",
                "A lista de favoritos foi atualizada para: $favorites"
            )
            favoriteListViewModel.loadAndFilterPokemonsFromFavoriteList(mfavoriteList)
        }

        favoriteListViewModel.loadFavorites()

        mfavoriteList = favoriteListViewModel.favoriteList.value ?: emptyList()

        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true)


        pokemonsListViewModel.pokemonsState.observe(requireActivity()) { pokemons ->
            pokemons?.let {
                updateRecyclerView(pokemons)
                progressBar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        pokemonsListViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                binding.progressBarLoadMore.visibility = View.VISIBLE
            } else {
                binding.progressBarLoadMore.visibility = View.GONE
            }
        }

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val limitLoading = lastVisibleItemPosition + 2

                if (limitLoading >= totalItemCount && !pokemonsListViewModel.isLoading.value!!) {
                    pokemonsListViewModel.loadMorePokemons()
                }

                currentPosition = lastVisibleItemPosition - 4

            }
        })
    }

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
        if (!isAdded) {
            return
        }
        initRecyclerView(pokemons, setLayout())
        updatePokemonFavorites(pokemons)
        mRecyclerView.layoutManager = mLayoutManager
        setupAdapter(this)
        mRecyclerView.scrollToPosition(currentPosition)
    }
    private fun initRecyclerView(pokemons: List<Pokemon?>, layoutManagerProvider: GridLayoutManager) {
        mPokemonAdapter = PokemonAdapter(pokemons)
        mLayoutManager = layoutManagerProvider
    }

    private fun setLayout(): GridLayoutManager {
        val layoutManagerProvider = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 3)
        } else {
            GridLayoutManager(requireActivity(), 2)
        }
        return layoutManagerProvider
    }

    private fun setupAdapter(onItemClickListener: PokemonAdapter.OnItemClickListener) {
        mRecyclerView.adapter = mPokemonAdapter
        mPokemonAdapter.setOnItemClickListener(onItemClickListener)
    }
    private fun updatePokemonFavorites(pokemons: List<Pokemon?>) {
        pokemons.forEach { pokemon ->
            pokemon?.let {
                it.favorite = favoriteListViewModel.isFavorite(mfavoriteList, it)
            }
        }
    }

    override fun onFavoriteClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            favoriteListViewModel.updateFavoritesList(position, it, mfavoriteList, mPokemonAdapter)
        }
    }

    override fun onDetailClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
            intent.putExtra("EXTRA_POKEMON_POSITION", position)
            startActivity(intent)
        }
    }

}