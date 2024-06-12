package com.example.pokedex.presenter.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.PokemonRepositoryImpl
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListFragment
import com.example.pokedex.presenter.ui.favorites.PokemonFavoriteListFragment
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModel
import com.example.pokedex.presenter.ui.pokemonsList.PokemonsListViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pokemonsListViewModel: PokemonsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        RetrofitClient.initialize(this)

        val factory = PokemonsListViewModelFactory(PokemonRepositoryImpl(RetrofitClient))
        pokemonsListViewModel = ViewModelProvider(this, factory)[PokemonsListViewModel::class.java]


        binding.buttonFavorites.setOnClickListener {
            Log.d("MainActivity", "Button Favorites clicked")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokemonFavoriteListFragment.newInstance())
                .commit()

            binding.buttonFavorites.visibility = View.GONE
            binding.buttonBack.visibility = View.VISIBLE

            binding.textViewTitle.text = getString(R.string.title_favorite)
        }

        binding.buttonBack.setOnClickListener {
            Log.d("MainActivity", "Button back clicked")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokemonsListFragment.newInstance())
                .commit()

            // Chame a função refreshPokemons() no ViewModel
            pokemonsListViewModel.refreshPokemons()

            binding.buttonBack.visibility = View.GONE
            binding.buttonFavorites.visibility = View.VISIBLE

            binding.textViewTitle.text = getString(R.string.Pokedex)

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PokemonsListFragment.newInstance())
            .commit()
    }

}