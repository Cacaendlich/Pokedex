package com.example.pokedex.presentation.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.databinding.ActivityPokemonDetailBinding

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokemonDetailsViewModel : PokemonDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pokemonDetailsViewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]
    }
}
