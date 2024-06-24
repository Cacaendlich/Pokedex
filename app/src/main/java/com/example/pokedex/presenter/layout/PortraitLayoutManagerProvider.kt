package com.example.pokedex.presenter.layout

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager

class PortraitLayoutManagerProvider(private val activity: FragmentActivity) : LayoutManagerProvider {
    override fun getLayoutManager(): GridLayoutManager {
        return  GridLayoutManager(activity, 2)
    }
}