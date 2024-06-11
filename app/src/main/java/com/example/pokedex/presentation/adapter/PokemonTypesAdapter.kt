package com.example.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R

class PokemonTypesAdapter(
    private val typesList: List<String>
) : RecyclerView.Adapter<PokemonTypesAdapter.PokemonTypesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_type, parent, false)
        return PokemonTypesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return typesList.size
    }

    override fun onBindViewHolder(holder: PokemonTypesViewHolder, position: Int) {
        val type = typesList[position]
        holder.bindType(type)
    }

    inner class PokemonTypesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTextViewPokemonType: TextView = itemView.findViewById(R.id.textView_pokemonType)

        fun bindType(type: String) {
            type.let {
                mTextViewPokemonType.text = type
            }
        }
    }
}