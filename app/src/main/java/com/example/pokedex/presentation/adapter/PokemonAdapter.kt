package com.example.pokedex.presentation.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon

class PokemonAdapter(
    val mPokemonList: List<Pokemon?>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onFavoriteClick(position: Int, imageView: ImageView)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount() = mPokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = mPokemonList[position]
        holder.bindPokemon(currentItem)
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //referencias de dados
        private val mImageViewPokemon: ImageView = itemView.findViewById(R.id.image_view_pokemon)
        private val mNameViewPokemon: TextView = itemView.findViewById(R.id.name_view_pokemon)
        private val mCardViewPokemon: CardView = itemView.findViewById(R.id.card_view_pokemon)
        private val mImageViewFavoriteOFF: ImageView = itemView.findViewById(R.id.favorite_off)


        init {
            mImageViewFavoriteOFF.setOnClickListener {
                bindingAdapterPosition.let {
                    onItemClickListener?.onFavoriteClick(it, mImageViewFavoriteOFF)
                }
            }
        }

        fun bindPokemon(currentItem: Pokemon?) {
            currentItem?.let { pokemon ->
                loadPokemonImageUrl(pokemon.imageUrl)
                mNameViewPokemon.text = currentItem.name
                Log.d("PokemonAdapter", "${pokemon.name} - Actual Favorite Status:  ${pokemon.favorite}")
                updateFavoriteIcon(pokemon.favorite, mImageViewFavoriteOFF)
            }
        }

        private fun loadPokemonImageUrl(imageUrl: String) {
            imageUrl.let { url ->
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(url)
                    .into(PokemonImageCustomTarget())
            }
        }

        private inner class PokemonImageCustomTarget : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                mImageViewPokemon.setImageBitmap(resource)
                extractDominantColor(resource) { color ->
                    mCardViewPokemon.setCardBackgroundColor(color)
                }
            }

            override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                mImageViewPokemon.setImageBitmap(null)
            }
        }

    }

    // Extrai a cor principal do Pokémon da imagem usando a biblioteca Palette
    private fun extractDominantColor(bitmap: Bitmap, onColorExtracted: (Int) -> Unit) {
        val croppedBitmap = cropCenteredBitmap(bitmap)

        // Passar o bitmap recortado para a Palette para extrair a cor predominante
        Palette.from(croppedBitmap).generate { palette ->
            val dominantColor = palette?.dominantSwatch?.rgb
            dominantColor?.let {
                onColorExtracted(it)
            }
        }
    }

    private fun cropCenteredBitmap(bitmap: Bitmap): Bitmap {
        // Determinar as dimensões do retângulo de recorte
        val cropLeft = bitmap.width / 4
        val cropTop = bitmap.height / 4
        val cropRight = bitmap.width * 3 / 4
        val cropBottom = bitmap.height * 3 / 4
        // Recortar a imagem
        return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropRight - cropLeft, cropBottom - cropTop)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean, imageView: ImageView) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.favorite_on)
        } else {
            imageView.setImageResource(R.drawable.favorite_off)
        }
    }

    fun updatePokemonFavoriteStatus(position: Int, isFavorite: Boolean) {
        // Atualiza apenas o item alterado
        mPokemonList[position]?.favorite = isFavorite
        notifyItemChanged(position)
    }


}
