package com.example.pokedex.presenter.ui.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PokemonFavoriteListViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: PokemonFavoriteListViewModel

    @Mock
    private lateinit var pokemonRepository: PokemonApiRepository
    @Mock
    private lateinit var pokemonLocalRepository: PokemonLocalRepository
    @Mock
    private lateinit var observer: Observer<List<Pokemon?>>


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonFavoriteListViewModel(pokemonRepository, pokemonLocalRepository)
        viewModel.pokemonsState.observeForever(observer)

    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
    }

    @Test
    fun removeFavorite(){

    }

}