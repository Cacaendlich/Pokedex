package com.example.pokedex.presenter.ui.pokemonsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.pokemonsList.useCase.LoadPokemonsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
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
    @Mock
    private lateinit var isLoginMock: Observer<Boolean>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loadPokemonsUseCase = LoadPokemonsUseCase(pokemonRepository)
        viewModel = PokemonsListViewModel(loadPokemonsUseCase)
        viewModel.pokemonsState.observeForever(observer)
        viewModel.isLoading.observeForever(isLoginMock)
    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
    }

    @Test
    fun `test initial loading of pokemons successfully`() = runTest {
        val listMock = listOf(
            Pokemon(1, "bulbasaur"),
            Pokemon(2, "ivysaur"),
            Pokemon(3, "venosaur")
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(listMock)

        viewModel.loadInitialPokemons()

        Assert.assertEquals(listMock, viewModel.pokemonsState.value)
    }
    @Test
    fun `test initial load of pokemons emptyList`() = runTest {
        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(emptyList())

        viewModel.loadInitialPokemons()

        Assert.assertEquals(emptyList<Pokemon>(), viewModel.pokemonsState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `teste carregamento de  mais pokemons com sucesso`() = runTest {
        val initialList = listOf(
            Pokemon(1, "bulbasaur"),
            Pokemon(2, "ivysaur")
        )
        val additionalItens = listOf(
            Pokemon(3, "venosaur"),
            Pokemon(4, "charmander")
        )
        val expectedUpdatedList = initialList + additionalItens

        `when`(loadPokemonsUseCase.execute(14, 0)).thenReturn(initialList)
        `when`(loadPokemonsUseCase.execute(14, initialList.size)).thenReturn(additionalItens)

        viewModel.pokemonsState.postValue(initialList)
        viewModel.loadMorePokemons()

        advanceUntilIdle()

        Assert.assertEquals(expectedUpdatedList, viewModel.pokemonsState.value)
    }


}