package com.example.pokedex.presenter.layout

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager

class LandscapeLayoutManagerProvider (private val activity: FragmentActivity) : LayoutManagerProvider {
    override fun getLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, 3)
    }
}