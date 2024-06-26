package com.example.pokedex.presenter.ui.pokemonsList

import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PokemonsListViewModelTest {

    private lateinit var viewModel: PokemonsListViewModel

    @Mock
    private lateinit var pokemonRepository: PokemonRepository


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonsListViewModel(pokemonRepository)
    }

    @After
    fun tearDown() {
    }

//    @Test
//    fun getPokemonsState() {
//    }
//
//    @Test
//    fun setPokemonsState() {
//    }
//
//    @Test
//    fun isLoading() {
//    }
//
//    @Test
//    fun setLoading() {
//    }

    @Test
    fun loadPokemons() = runTest {
        val limit = 14
        val offset = 0

        val listMock = listOf(
            Pokemon(1, "bulbasaur", true),
            Pokemon(2, "ivysaur", false),
            Pokemon(3, "venosaur", false)
        )

        `when`(pokemonRepository.listPokemons(limit, offset)).thenReturn(listMock)

        val result = viewModel.loadPokemons(limit, offset)

        assertEquals(listMock, result)
    }
//
//    @Test
//    fun loadMorePokemons() {
//    }
//
//    @Test
//    fun refreshPokemons() {
//    }
//
//    @Test
//    fun testSetLoading() {
//    }
}