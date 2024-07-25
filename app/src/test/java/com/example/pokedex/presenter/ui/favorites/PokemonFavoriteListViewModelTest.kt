package com.example.pokedex.presenter.ui.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PokemonFavoriteListViewModelTest {
    private val pokemonListMock = listOf(
        Pokemon(1, "bulbasaur"),
        Pokemon(2, "ivysaur"),
        Pokemon(3, "venusaur"),
        Pokemon(4, "charmander")
    )
    private val pokemonFavoriteListMock = listOf(
        PokemonEntity(1, "bulbasaur"),
        PokemonEntity(2, "ivysaur")
    )

    private val pokemonMock = Pokemon(4, "charmander")
    private val pokemonMock2 = Pokemon(1, "bulbasaur")
    private val pokemonFavoriteMock = PokemonEntity(1, "Bulbasaur")




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
        `when`(viewModel.isFavorite(pokemonFavoriteListMock, pokemonMock2)).thenReturn(true)

    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
    }

//    @Test
//    fun loadPokemons() {
//    }
//
//    @Test
//    fun loadFavorites() {
//    }

    //updateFavoritesList()
    @Test
    fun `updateFavoritesList pokemon is favorite, delete Favorite successfully`() = runTest{
        val pokemon = pokemonMock2
        val favoriteList = pokemonFavoriteListMock


        viewModel.updateFavoritesList(pokemon, favoriteList)

        Mockito.verify(pokemonLocalRepository).deleteFavorite(pokemon.number)
    }

    @Test
    fun  `test checking if Pokemon is favorite when it is in the list`() {
        val favoritePokemon = Pokemon(1, "bulbasaur")

        val resul = viewModel.isFavorite(pokemonFavoriteListMock, favoritePokemon)

        Assertions.assertTrue(resul)
    }

    @Test
    fun `test checking if Pokemon is favorite when it is not in the list`() {
        val favoritePokemon = Pokemon(3, "venosaur")

        val resul = viewModel.isFavorite(pokemonFavoriteListMock, favoritePokemon)

        Assertions.assertFalse(resul)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun removeFavorite() = runTest{
//
//        viewModel.removeFavorite(pokemonMock)
//
//        advanceUntilIdle()
//
//        Mockito.verify(pokemonLocalRepository).deleteFavorite(pokemonMock.number)
//    }
}