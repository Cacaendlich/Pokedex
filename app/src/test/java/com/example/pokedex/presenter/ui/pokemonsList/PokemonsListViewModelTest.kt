package com.example.pokedex.presenter.ui.pokemonsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PokemonsListViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: PokemonsListViewModel

    @Mock
    private lateinit var pokemonRepository: PokemonApiRepository
    @Mock
    private lateinit var observer: Observer<List<Pokemon?>>
    @Mock
    private lateinit var isLoginObserver: Observer<Boolean>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonsListViewModel(pokemonRepository)
        viewModel.pokemonsState.observeForever(observer)
        viewModel.isLoading.observeForever(isLoginObserver)
    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
        viewModel.isLoading.removeObserver(isLoginObserver)
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

        `when`(pokemonRepository.listPokemons(14, 0)).thenReturn(initialList)
        `when`(pokemonRepository.listPokemons(14, initialList.size)).thenReturn(additionalItens)

        viewModel.pokemonsState.postValue(initialList)
        viewModel.loadMorePokemons()

        advanceUntilIdle()
        //avançar o tempo até que todas as coroutines pendentes tenham concluído a execução

        Assert.assertEquals(expectedUpdatedList, viewModel.pokemonsState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshPokemonsTest() = runTest{
        val initialList = listOf(
            Pokemon(1, "bulbasaur"),
            Pokemon(2, "ivysaur")
        )

        `when`(pokemonRepository.listPokemons(14, 0)).thenReturn(initialList)

        viewModel.refreshPokemons()

        advanceUntilIdle()

        Mockito.verify(isLoginObserver).onChanged(true)

        Assert.assertEquals(initialList, viewModel.pokemonsState.value)

    }
}