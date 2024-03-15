package com.example.pokedex.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.domain.model.PokemonList

class PokemonAdapter(
    private val mContext: Context,
    private val mPokemonList: ArrayList<PokemonList>
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount() = mPokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = mPokemonList[position]

        holder.bindView(currentItem)

//        val imagePokemon = currentItem.getPokemonImg()
//        val namePokemon = currentItem.getPokemonName()
//
//        holder.mNameViewPokemon.text = namePokemon
//        Glide.with(mContext).load(imagePokemon).into(holder.mImageViewPokemon)
    }

    inner class PokemonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(currentItem: PokemonList) {

        }

        val mImageViewPokemon: ImageView = itemView.findViewById(R.id.image_view_pokemon)
        val mNameViewPokemon: TextView = itemView.findViewById(R.id.name_view_pokemon)
    }

}