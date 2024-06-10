package com.example.pokedex.presentation.ui.details

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.databinding.ActivityPokemonDetailBinding
import com.example.pokedex.presentation.ui.main.MainActivity

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokemonDetailsViewModel : PokemonDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pokemonDetailsViewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]

        binding.imageViewBackToList.setOnClickListener {
            val sourceFragment = intent.getStringExtra("sourceFragment")
            Log.d(TAG, "Source Fragment: $sourceFragment")

            when (sourceFragment) {
                "PokemonsFavoriteListFragment" -> {
                    Log.d(TAG, "Popping back to PokemonsFavoriteListFragment")
                    supportFragmentManager.popBackStack("PokemonsFavoriteListFragment", 0)
                }
                "PokemonsListFragment" -> {
                    Log.d(TAG, "Popping back to PokemonsListFragment")
                    supportFragmentManager.popBackStack("PokemonsListFragment", 0)
                }
                else -> {
                    Log.d(TAG, "Starting MainActivity")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }


    }
    companion object {
        private const val TAG = "PokemonDetailActivity"
    }
}
