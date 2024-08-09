package com.example.pokedex.presenter.ui.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.network.RetrofitClient
import com.example.pokedex.data.repository.api.PokemonApiRepositoryImpl
import com.example.pokedex.data.repository.local.PokemonLocalRepositoryImpl
import com.example.pokedex.databinding.ActivityPokemonDetailBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.adapter.PokemonTypesAdapter
import com.example.pokedex.presenter.ui.factory.PokemonsListViewModelFactory
import com.example.pokedex.presenter.ui.main.MainActivity
import kotlinx.coroutines.launch

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var pokemonDetailsViewModel : PokemonDetailsViewModel

    // UI
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
    private lateinit var mRecyclerView: RecyclerView

    // Data
    private lateinit var mPokemon: Pokemon
    private lateinit var mPokemonTypesAdapter: PokemonTypesAdapter
    private lateinit var mLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initUi()

        val retrofitClient = RetrofitClient
        val pokemonApiRepository = PokemonApiRepositoryImpl(retrofitClient)
        val pokemonLocalRepository = PokemonLocalRepositoryImpl(this)
        val factory = PokemonsListViewModelFactory(pokemonApiRepository, pokemonLocalRepository)


        pokemonDetailsViewModel = ViewModelProvider(this, factory)[PokemonDetailsViewModel::class.java]

        val pokemonName = intent.getStringExtra("EXTRA_POKEMON_NAME")

        pokemonName?.let {
            lifecycleScope.launch {
                pokemonDetailsViewModel.loadPokemon(pokemonName.toString())
            }
        }

        pokemonDetailsViewModel.pokemonLiveData.observe(this){pokemon ->
            pokemon?.let {
                mPokemon = pokemon
                updateUi(mPokemon)
                Log.e("PokemonDetailActivity, Loaded Pokemon: ", mPokemon.toString())
            }
        }


        mRecyclerView = binding.recyclerViewPokemonTypes

        binding.imageViewBackToList.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun initUi(){
        mPokemonNumber = binding.textViewNumber
        mPokemonName = binding.textViewPokemonName
        mProgressBarHp = binding.progressBarHP
        mProgressBarAtk = binding.progressBarAtk
        mProgressBarDef = binding.progressBarDef
        mProgressBarSpd = binding.progressBarSpd
        mHeight = binding.textViewHeightValue
        mWeight = binding.textViewWeightValue
    }

    private fun updateUi(pokemon: Pokemon){
        pokemon.let {
            mPokemonName.text = pokemon.name
            mHeight.text = (pokemon.height/10.0).toString()
            mWeight.text = (pokemon.weight/10.0).toString()
        }
    }

    private fun updateRecyclerView(types: List<String>) {
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mPokemonTypesAdapter = PokemonTypesAdapter(types)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mPokemonTypesAdapter
    }
}
