package com.example.pokedex.presentation.ui.pokemonsList

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter
import com.example.pokedex.presentation.ui.favorites.PokemonFavoriteListViewModel

class PokemonsListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var pokemonsListViewModel: PokemonsListViewModel
    private lateinit var favoriteListViewModel: PokemonFavoriteListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var mfavoriteList: List<PokemonEntity>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        swipeRefreshLayout = binding.swipeRefreshLayout
        progressBar = binding.progressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.initialize(requireActivity())

        pokemonsListViewModel = ViewModelProvider(requireActivity())[PokemonsListViewModel::class.java]
        favoriteListViewModel = ViewModelProvider(requireActivity())[PokemonFavoriteListViewModel::class.java]

        favoriteListViewModel.loadFavorites(requireContext()) { favorites ->
            mfavoriteList = favorites
            favoriteListViewModel.favoriteList.postValue(mfavoriteList)
            Log.d("PokemonsListFragment", "A lista de favoritos foi atualizada para: ${favoriteListViewModel.favoriteList.value}")
            Log.d("PokemonsListFragment", "Está é a lista de favoritos: $mfavoriteList")
        }

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

        swipeRefreshLayout.setOnRefreshListener {
            // Atualizar lista quando o gesto de atualização for detectado
            pokemonsListViewModel.refreshPokemons()
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
        mRecyclerView.scrollToPosition(currentPosition)

        swipeRefreshLayout.isRefreshing = false
    }

    override fun onFavoriteClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            favoriteListViewModel.updateFavoritesList(position, it, mfavoriteList, mPokemonAdapter, requireContext())
        }
    }

}