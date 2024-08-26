package com.example.pokedex.presenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.model.PokemonType

class PokemonTypesAdapter(
    private val typesList: List<PokemonType>
) : RecyclerView.Adapter<PokemonTypesAdapter.PokemonTypesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_type, parent, false)
        return PokemonTypesViewHolder(view)
    }

    override fun getItemCount() = typesList.size

    override fun onBindViewHolder(holder: PokemonTypesViewHolder, position: Int) {
        val type = typesList[position]
        holder.bindType(type)
    }

    inner class PokemonTypesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTextViewPokemonType: TextView = itemView.findViewById(R.id.textView_pokemonType)
        private val mLinearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout_pokemonTypes_item)

        fun bindType(type: PokemonType) {
            type.let {
                mTextViewPokemonType.text = type.type.name
                mLinearLayout.setBackgroundColor(updateColorType(type, itemView))
            }
        }
    }

    private fun updateColorType(type: PokemonType, itemView: View): Int {
        val backgroundColor = when(type.type.name){
            "bug" -> ContextCompat.getColor(itemView.context, R.color.bug)
            "dark" -> ContextCompat.getColor(itemView.context, R.color.dark)
            "dragon" -> ContextCompat.getColor(itemView.context, R.color.dragon)
            "electric" -> ContextCompat.getColor(itemView.context, R.color.electric)
            "fairy" -> ContextCompat.getColor(itemView.context, R.color.fairy)
            "fighting" -> ContextCompat.getColor(itemView.context, R.color.fighting)
            "fire" -> ContextCompat.getColor(itemView.context, R.color.fire)
            "flying" -> ContextCompat.getColor(itemView.context, R.color.flying)
            "ghost" -> ContextCompat.getColor(itemView.context, R.color.ghost)
            "grass" -> ContextCompat.getColor(itemView.context, R.color.grass)
            "ground" -> ContextCompat.getColor(itemView.context, R.color.ground)
            "ice" -> ContextCompat.getColor(itemView.context, R.color.ice)
            "normal" -> ContextCompat.getColor(itemView.context, R.color.normal)
            "poison" -> ContextCompat.getColor(itemView.context, R.color.poison)
            "psychic" -> ContextCompat.getColor(itemView.context, R.color.psychic)
            "rock" -> ContextCompat.getColor(itemView.context, R.color.rock)
            "steel" -> ContextCompat.getColor(itemView.context, R.color.steel)
            "water" -> ContextCompat.getColor(itemView.context, R.color.water)
            else -> ContextCompat.getColor(itemView.context, R.color.black)
        }
        return backgroundColor
    }
}