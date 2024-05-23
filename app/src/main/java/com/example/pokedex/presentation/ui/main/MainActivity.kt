package com.example.pokedex.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.presentation.ui.pokemonsList.PokemonsListFragment
import com.example.pokedex.presentation.ui.favorites.PokemonFavoriteListFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonFavorites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokemonFavoriteListFragment.newInstance())
                .commit()

            binding.buttonFavorites.visibility = View.GONE
            binding.buttonBack.visibility = View.VISIBLE
        }

        binding.buttonBack.setOnClickListener {
            Log.d("MainActivity", "Button back clicked")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokemonsListFragment.newInstance())
                .commit()

            binding.buttonBack.visibility = View.GONE
            binding.buttonFavorites.visibility = View.VISIBLE
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PokemonsListFragment.newInstance())
            .commit()
    }

}