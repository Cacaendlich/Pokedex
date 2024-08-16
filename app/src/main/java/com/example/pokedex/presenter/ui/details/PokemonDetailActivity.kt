package com.example.pokedex.presenter.ui.details

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Stats
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
    private lateinit var mProgressBarHp: ProgressBar
    private lateinit var mProgressBarAtk: ProgressBar
    private lateinit var mProgressBarDef: ProgressBar
    private lateinit var mProgressBarSpd: ProgressBar
    private lateinit var mHeight: TextView
    private lateinit var mWeight: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRelativeLayout: RelativeLayout

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
        val color = intent.getIntExtra("EXTRA_POKEMON_COR", -1)
        color.let{
            pokemonDetailsViewModel.updateBackgroundColor(mRelativeLayout,color)
        }

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
        mPokemonImage = binding.imageViewPokemon
        mRelativeLayout = binding.relativeLayout
    }

    private fun updateUi(pokemon: Pokemon){
        pokemon.let {
            mPokemonNumber.text = getString(R.string.pokemon_id, pokemon.number)
            mPokemonName.text = pokemon.name
            mHeight.text = (pokemon.height/10.0).toString()
            mWeight.text = (pokemon.weight/10.0).toString()
            pokemon.stats.forEach{ stats ->
               updateStats(stats)
            }
            updateImageUrl(pokemon.imageUrl)
            updateRecyclerView(pokemon.type)
        }
    }

    private fun updateRecyclerView(types: List<PokemonType>) {
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mPokemonTypesAdapter = PokemonTypesAdapter(types)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mPokemonTypesAdapter
    }

    private fun updateStats(stats: Stats){
        when(stats.stat.name){
            "hp" -> animateProgressBar(mProgressBarHp, stats.base_stat)
            "attack" -> animateProgressBar(mProgressBarAtk, stats.base_stat)
            "defense" -> animateProgressBar(mProgressBarDef, stats.base_stat)
            "speed" -> animateProgressBar(mProgressBarSpd, stats.base_stat)
        }
    }

    private fun updateImageUrl(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(mPokemonImage)
    }

    private fun animateProgressBar(progressBar: ProgressBar, toProgress: Int){
        val animator = ValueAnimator.ofInt(0, toProgress)
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            progressBar.progress = animation.animatedValue as Int
        }
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

}
