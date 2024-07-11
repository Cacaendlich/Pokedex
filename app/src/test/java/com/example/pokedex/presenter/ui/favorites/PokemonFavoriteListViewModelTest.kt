package com.example.pokedex.presenter.ui.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mock
import org.mockito.Mockito
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
    @Mock
    private lateinit var isLoginMock: Observer<Boolean>


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonFavoriteListViewModel(pokemonRepository, pokemonLocalRepository)
        viewModel.pokemonsState.observeForever(observer)
        viewModel.isLoading.observeForever(isLoginMock)
    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
        viewModel.isLoading.removeObserver(isLoginMock)
    }

//    @Test
//    fun loadPokemons() {
//    }
//
//    @Test
//    fun loadFavorites() {
//    }
//    @Test
//    fun updateFavoritesList() {
//    }
//

    @Test
    fun  `test checking if Pokemon is favorite when it is in the list`() {
        val favoritListMock = listOf(
            PokemonEntity(1, "bulbasaur"),
            PokemonEntity(2, "ivysaur")
        )
        val favoritePokemon = Pokemon(1, "bulbasaur")

        val resul = viewModel.isFavorite(favoritListMock, favoritePokemon)

        Assertions.assertTrue(resul)
    }

    @Test
    fun `test checking if Pokemon is favorite when it is not in the list`() {
        val favoritListMock = listOf(
            PokemonEntity(1, "bulbasaur"),
            PokemonEntity(2, "ivysaur")
        )
        val favoritePokemon = Pokemon(3, "venosaur")

        val resul = viewModel.isFavorite(favoritListMock, favoritePokemon)

        Assertions.assertFalse(resul)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeFavorite() = runTest{
        val pokemonMock = Pokemon(1, "Bulbasaur")

        viewModel.removeFavorite(pokemonMock)

        advanceUntilIdle()

        Mockito.verify(pokemonLocalRepository).deleteFavorite(pokemonMock.number)
    }
}