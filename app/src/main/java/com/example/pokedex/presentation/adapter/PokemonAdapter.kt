package com.example.pokedex.presentation.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon

class PokemonAdapter(
    private val mPokemonList: List<Pokemon?>
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount() = mPokemonList.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = mPokemonList[position]
        holder.bindView(currentItem)
    }

    inner class PokemonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mImageViewPokemon: ImageView = itemView.findViewById(R.id.image_view_pokemon)
        private val mNameViewPokemon: TextView = itemView.findViewById(R.id.name_view_pokemon)

        private val sampleSize = 100

        fun bindView(currentItem: Pokemon?) {

            Glide.with(itemView.context)
                .asBitmap()
                .load(currentItem?.imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        mImageViewPokemon.setImageBitmap(resource)
                        extractMainColor(resource) { color ->
                            itemView.setBackgroundColor(color)

                        }
                    }


                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                        // Placeholder cleanup
                    }
                })

            if (currentItem != null) {
                mNameViewPokemon.text = currentItem.name
            }

        }

        // Extrai a cor principal do Pokémon da imagem usando a biblioteca Palette
        private fun extractMainColor(bitmap: Bitmap, onColorExtracted: (Int) -> Unit) {
            // Determinar as dimensões do retângulo de recorte (pode precisar ser ajustado dependendo da imagem)
            val cropLeft = bitmap.width / 4
            val cropTop = bitmap.height / 4
            val cropRight = bitmap.width * 3 / 4
            val cropBottom = bitmap.height * 3 / 4

            // Recortar a imagem
            val croppedBitmap = Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropRight - cropLeft, cropBottom - cropTop)

            // Passar o bitmap recortado para a Palette para extrair a cor predominante
            Palette.from(croppedBitmap).generate { palette ->
                val dominantColor = palette?.dominantSwatch?.rgb
                dominantColor?.let {
                    onColorExtracted(it)
                }
            }
        }


    }
    }