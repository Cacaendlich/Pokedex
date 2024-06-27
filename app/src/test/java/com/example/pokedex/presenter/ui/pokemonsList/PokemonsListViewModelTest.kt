package com.example.pokedex.presenter.ui.pokemonsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.pokemonsList.useCase.LoadPokemonsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PokemonsListViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: PokemonsListViewModel
    private lateinit var loadPokemonsUseCase: LoadPokemonsUseCase

    @Mock
    private lateinit var pokemonRepository: PokemonRepository
    @Mock
    private lateinit var observer: Observer<List<Pokemon?>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loadPokemonsUseCase = LoadPokemonsUseCase(pokemonRepository)
        viewModel = PokemonsListViewModel(loadPokemonsUseCase)
        viewModel.pokemonsState.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
    }

    @Test
    fun `teste carregamento inicial de pokemons com sucesso`() = runTest {
        val listMock = listOf(
            Pokemon(1, "bulbasaur", true),
            Pokemon(2, "ivysaur", false),
            Pokemon(3, "venosaur", false)
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(listMock)

        viewModel.loadInitialPokemons()

        verify(observer).onChanged(listMock)
    }
    @Test
    fun `teste carregamento inicial de pokemons com falha`() = runTest {
        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(emptyList())

        viewModel.loadInitialPokemons()

        verify(observer).onChanged(emptyList())
    }

}