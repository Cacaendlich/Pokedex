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
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.data.network.RetrofitClient


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mPokemonAdapter: PokemonAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        RetrofitClient.initialize(this)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

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

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Obtém o LayoutManager associado ao RecyclerView
                val layoutManager = recyclerView.layoutManager as GridLayoutManager

                // Obtém a posição do último item completamente visível na tela
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                Log.d("lastVisibleItemPosition", lastVisibleItemPosition.toString())

                // Obtém o número total de itens no RecyclerView
                val totalItemCount = layoutManager.itemCount
                Log.d("totalItemCount", totalItemCount.toString())

                // Define um buffer para determinar quando carregar mais itens
                val limitLoading = lastVisibleItemPosition + 2

                // Verifica se o usuário está perto do final da lista
                if (limitLoading >= totalItemCount && !viewModel.isLoading.value!!) {
                    viewModel.loadMorePokemons()
                }
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