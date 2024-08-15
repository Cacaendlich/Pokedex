package com.example.pokedex.presenter.ui.details

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon

class PokemonDetailsViewModel(
    private var pokemonApiRepository: PokemonApiRepository
) : ViewModel(){
    var pokemonLiveData: MutableLiveData<Pokemon?> = MutableLiveData()

    suspend fun loadPokemon(name: String) {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Nome do Pokémon está vazio")
        }
        val pokemon = pokemonApiRepository.getPokemons(name)
        pokemonLiveData.postValue(pokemon)
    }

    private fun validateColor(intentColor: Int) = intentColor != -1

    fun updateBackgroundColor(relativeLayout: RelativeLayout, color: Int){
        if (validateColor(color)){
            backgroundColor(relativeLayout, color)
        }
    }
    private fun backgroundColor(relativeLayout: RelativeLayout, color: Int){
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color,color, Color.BLACK)
        )
        relativeLayout.background = gradientDrawable
    }
}