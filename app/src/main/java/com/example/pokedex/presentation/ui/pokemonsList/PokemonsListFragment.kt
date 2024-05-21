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
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter

class PokemonsListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var viewModel: PokemonsListViewModel
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

        RetrofitClient.initialize(requireActivity())

        viewModel = ViewModelProvider(requireActivity())[PokemonsListViewModel::class.java]

        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true)

        viewModel.pokemonsState.observe(requireActivity()) { pokemons ->
            pokemons?.let {
                updateRecyclerView(pokemons)
                progressBar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewModel.isLoading.observe(requireActivity()) { isLoading ->
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

                if (limitLoading >= totalItemCount && !viewModel.isLoading.value!!) {
                    viewModel.loadMorePokemons()
                }

                currentPosition = lastVisibleItemPosition - 4

            }
        })

        viewModel.loadFavorites(requireContext()) { favorites ->
            mfavoriteList = favorites
            Log.d("PokemonsListFragment", "Está é a lista de favoritos: $mfavoriteList")
        }
    }

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
        pokemons.forEach { pokemon ->
            pokemon?.let {
                it.favorite = viewModel.isFavorite(mfavoriteList, it)
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
    }

    override fun onFavoriteClick(position: Int, imageView: ImageView) {
        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)

            val isFavorite = viewModel.isFavorite(mfavoriteList, pokemon)
            Log.d("PokemonsListFragment", "${pokemon.name} - Está na lista de favoritos: $isFavorite")

            if (!isFavorite && !pokemon.favorite) {
                viewModel.addFavorites(pokemonFavorite, requireContext()) {
                    Log.d("PokemonsListFragment", "${pokemon.name} - Adicionado aos favoritos com SUCESSO!")
                    mPokemonAdapter.updateFavoriteStatus(position, true)
                }
            } else {
                viewModel.deleteFavorites(pokemon.number, requireContext()) {
                    Log.d("PokemonsListFragment", "${pokemon.name} - Excluído dos favoritos com SUCESSO!")
                    mPokemonAdapter.updateFavoriteStatus(position, false)
                }
            }
        }
    }

}