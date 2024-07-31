package com.example.pokedex.presenter.ui.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ActivityPokemonDetailBinding
import com.example.pokedex.presenter.adapter.PokemonTypesAdapter
import com.example.pokedex.presenter.ui.main.MainActivity

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokemonDetailsViewModel : PokemonDetailsViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPokemonTypesAdapter: PokemonTypesAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var mPokemonNumber: TextView
    private lateinit var mPokemonImage: ImageView
    private lateinit var mPokemonName: TextView
    private lateinit var mTypeList: List<String>
    private lateinit var mProgressBarHp: ProgressBar
    private lateinit var mProgressBarAtk: ProgressBar
    private lateinit var mProgressBarDef: ProgressBar
    private lateinit var mProgressBarSpd: ProgressBar
    private lateinit var mHeight: TextView
    private lateinit var mWeight: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mPokemonNumber = binding.textViewNumber
        mPokemonName = binding.textViewPokemonName
        mProgressBarHp = binding.progressBarHP
        mProgressBarAtk = binding.progressBarAtk
        mProgressBarDef = binding.progressBarDef
        mProgressBarSpd = binding.progressBarSpd
        mHeight = binding.textViewHeightValue
        mWeight = binding.textViewWeightValue

        pokemonDetailsViewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]

        val position = intent.getIntExtra("EXTRA_POKEMON_POSITION", -1)
        val pokemonName = intent.getStringExtra("EXTRA_POKEMON_NAME")
        Log.d("PokemonDetailActivity", "Position received: $position")

        mPokemonName.text = pokemonName

        mRecyclerView = binding.recyclerViewPokemonTypes

        binding.imageViewBackToList.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun updateRecyclerView(types: List<String>) {
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mPokemonTypesAdapter = PokemonTypesAdapter(types)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mPokemonTypesAdapter
    }
}
