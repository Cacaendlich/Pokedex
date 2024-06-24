package com.example.pokedex.presenter.layout

import androidx.recyclerview.widget.GridLayoutManager

interface LayoutManagerProvider {
    fun getLayoutManager(): GridLayoutManager
}