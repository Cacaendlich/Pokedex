package com.example.pokedex.representation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonListItem
import com.example.pokedex.ui.PokemonAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPokemonAdapter: PokemonAdapter
    private lateinit var mPokemonList: ArrayList<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mRecyclerView = binding.recyclerViewMain
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(this, 2)

        mPokemonAdapter = PokemonAdapter(this@MainActivity, mPokemonList)
        mRecyclerView.adapter = mPokemonAdapter

        mPokemonList = ArrayList()

        viewModel.fetchPokemonList()
    }
}