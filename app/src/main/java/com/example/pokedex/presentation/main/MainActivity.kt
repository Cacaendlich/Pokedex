package com.example.pokedex.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter
import android.content.res.Configuration
import androidx.core.widget.NestedScrollView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var mNsvView: NestedScrollView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mNsvView = binding.nsvView
        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true) //informar ao RecyclerView que o tamanho dos itens não mudará durante a execução.


        val progressBar = binding.progressBar


        viewModel.pokemonsState.observe(this) { pokemons ->
            pokemons?.let {
                updateRecyclerView(it)
                progressBar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }

        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBarLoadMore.visibility = View.VISIBLE
            } else {
                binding.progressBarLoadMore.visibility = View.GONE
            }
        }

        mNsvView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ view, scrollX, scrollY, oldScrollX, oldScrollY ->
            val totalHeight = view.getChildAt(0).height
            val currentScroll = view.scrollY + view.height

            if (currentScroll >= totalHeight && !viewModel.isLoading.value!!) {
                viewModel.loadMorePokemons()
            }
        })
    }

    private fun updateRecyclerView(pokemons: List<Pokemon?>) {
        mLayoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 3)
        } else {
            GridLayoutManager(this, 2)
        }
        mPokemonAdapter = PokemonAdapter(pokemons)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mPokemonAdapter
    }
}