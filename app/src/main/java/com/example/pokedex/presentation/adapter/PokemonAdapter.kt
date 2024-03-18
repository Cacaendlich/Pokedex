package com.example.pokedex.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon

class PokemonAdapter(
    private val mPokemonList: List<Pokemon>
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount() = mPokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = mPokemonList[position]
        Log.d("PokemonAdapter", "onBindViewHolder: Position: $position, Pokemon: $currentItem")
        holder.bindView(currentItem)
    }

    inner class PokemonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(currentItem: Pokemon) = with(itemView) {
            Log.d("PokemonAdapter", "bindView: Pokemon: $currentItem")
            val mImageViewPokemon = findViewById<ImageView>(R.id.image_view_pokemon)
            val mNameViewPokemon = findViewById<TextView>(R.id.name_view_pokemon)

            Glide.with(itemView)
                .load(currentItem.imageUrl)
                .placeholder(R.mipmap.ic_launcher_round) // Imagem de placeholder, se desejar
                .error(R.mipmap.ic_launcher_round) // Imagem de erro, se a carga falhar
                .into(mImageViewPokemon)

            mNameViewPokemon.text = currentItem.name

        }
    }

}