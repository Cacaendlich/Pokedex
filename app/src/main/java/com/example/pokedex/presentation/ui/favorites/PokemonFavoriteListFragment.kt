package com.example.pokedex.presentation.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.databinding.FragmentPokemonsListBinding
import com.example.pokedex.presentation.adapter.PokemonAdapter
import com.example.pokedex.presentation.ui.pokemonsList.PokemonsListViewModel

class PokemonFavoriteListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonsListBinding
    private lateinit var favoriteListViewModel: PokemonFavoriteListViewModel
    private lateinit var pokemonsListViewModel: PokemonsListViewModel
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListViewModel = ViewModelProvider(requireActivity())[PokemonFavoriteListViewModel::class.java]
        pokemonsListViewModel = ViewModelProvider(requireActivity())[PokemonsListViewModel::class.java]


    }

}