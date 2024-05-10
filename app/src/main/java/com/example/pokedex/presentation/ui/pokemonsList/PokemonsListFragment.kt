package com.example.pokedex.presentation.ui.pokemonsList

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonsListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var viewModel: PokemonsListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter
    private lateinit var progressBar: ProgressBar


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

        lifecycleScope.launch {
            // Obtém a lista de Pokémon favoritos em uma coroutine
            val favorites = withContext(Dispatchers.IO) {
                PokemonDataBase.getDataBase(requireContext()).PokemonDao().getAllPokemonsFavorites()
            }

            // Registra a lista de Pokémon favoritos em um log
            Log.d("PokemonsListFragment", "Pokémon Favoritos Salvos: $favorites")

            // Restante do seu código...
        }


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

    }

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
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
        Log.d("PokemonsListFragment", "Clique no item de favorito na posição: $position")

        val pokemon = mPokemonAdapter.mPokemonList[position]
        pokemon?.let {
            val pokemonFavorite = PokemonEntity(pokemon.number, pokemon.name)
            viewModel.addFavorites(pokemonFavorite, requireContext()){
                Log.d("PokemonsListFragment", "Clique no item de favorito : ${pokemon.name}, e seu estado: ${pokemon.favorite}")
            }
        }

    }

}