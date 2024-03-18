package com.example.pokedex.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presentation.adapter.PokemonAdapter

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

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true)

        Thread(Runnable {
            loadPokemons()

        }).start()

    }

    private fun loadPokemons() {
        val pokemonsApiResult = RetrofitClient.listPokemons()

        pokemonsApiResult?.results?.let {


            val pokemons: List<Pokemon?> = it.map { pokemonResult ->
                val number = pokemonResult.url.replace("https://pokeapi.co/api/v2/pokemon/", "").toInt()

                val pokemonApiResult = RetrofitClient.getPokemon(number)

                pokemonApiResult?.let {
                    Pokemon(
                        pokemonApiResult.id,
                        pokemonApiResult.name,
                    )
                }
            }

            mLayoutManager = GridLayoutManager(this, 2)
            mPokemonAdapter = PokemonAdapter(pokemons)

            mRecyclerView.post {
                mRecyclerView.layoutManager = mLayoutManager
                mRecyclerView.adapter = mPokemonAdapter
            }
        }

    }
}