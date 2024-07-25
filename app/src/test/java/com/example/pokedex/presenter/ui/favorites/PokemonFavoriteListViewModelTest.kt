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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
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
    private lateinit var isLoadingObserver: Observer<Boolean>
    @Mock
    private lateinit var favoriteListObserver: Observer<List<PokemonEntity?>>


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonFavoriteListViewModel(pokemonRepository, pokemonLocalRepository)
        viewModel.pokemonsState.observeForever(observer)
        viewModel.isLoading.observeForever(isLoadingObserver)
        viewModel.favoriteList.observeForever(favoriteListObserver)

    }

    @After
    fun tearDown() {
        viewModel.pokemonsState.removeObserver(observer)
        viewModel.isLoading.removeObserver(isLoadingObserver)
        viewModel.favoriteList.removeObserver(favoriteListObserver)
    }

    @Test
    fun `isFavorite achando pokemon na lista de favoritos`() {
        val pokemon1 = Pokemon(1, "bulbasur")
        val pokemon2 = Pokemon(2, "ivysaur")

        val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasur"),
            PokemonEntity(2, "ivysaur")
        )

        val result1 = viewModel.isFavorite(pokemonListFavorite, pokemon1)
        val result2 = viewModel.isFavorite(pokemonListFavorite, pokemon2)

        Assert.assertTrue(result1)
        Assert.assertTrue(result2)
    }

    @Test
    fun `isFavorite NAO achando pokemon na lista de favoritos`() {
        val pokemon1 = Pokemon(1, "bulbasur")
        val pokemon2 = Pokemon(2, "ivysaur")

        val pokemonListFavorite = listOf(
            PokemonEntity(3, "venusaur"),
            PokemonEntity(4, "charmander")
        )

        val result1 = viewModel.isFavorite(pokemonListFavorite, pokemon1)
        val result2 = viewModel.isFavorite(pokemonListFavorite, pokemon2)

        Assert.assertFalse(result1)
        Assert.assertFalse(result2)
    }


    @Test
    fun `updateFavoriteList quando o pokemon nao esta na lista de favoritos`() = runTest {
        val pokemon1 = Pokemon(1, "bulbasur")
        val pokemonListFavorite = emptyList<PokemonEntity>()
        val pokemonFavoritado = PokemonEntity(pokemon1.number, pokemon1.name)

        viewModel.updateFavoritesList(pokemon1, pokemonListFavorite)

        Mockito.verify(pokemonLocalRepository).addFavorite(pokemonFavoritado)
    }

    @Test
    fun `updateFavoriteList quando o pokemon esta na lista de favoritos`() = runTest {
        val pokemon1 = Pokemon(1, "bulbasur", true)
        val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasur")
        )

        viewModel.updateFavoritesList(pokemon1, pokemonListFavorite)

        Mockito.verify(pokemonLocalRepository).deleteFavorite(pokemon1.number)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadFavorites com sucesso`() = runTest{
        val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasur"),
            PokemonEntity(2, "ivysaur")
        )

        `when`(pokemonLocalRepository.getAllPokemons()).thenReturn(pokemonListFavorite)

        viewModel.loadFavorites()

        advanceUntilIdle()

        Mockito.verify(isLoadingObserver).onChanged(true)
        Mockito.verify(pokemonLocalRepository).getAllPokemons()

        val favoriteListExpectatio = pokemonListFavorite.map {
            PokemonEntity(it.pokemonId, it.name)
        }

        Mockito.verify(favoriteListObserver).onChanged(favoriteListExpectatio)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAndFilterPokemonsFromFavoriteList com sucesso`() = runTest{
        val limit = 1000
        val offset = 0

        val pokemonList = listOf(
            Pokemon(1, "bulbasur"),
            Pokemon(2, "ivysaur"),
            Pokemon(3, "venusaur"),
            Pokemon(4, "charmander")
        )

        val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasur"),
            PokemonEntity(2, "ivysaur")
        )

        `when`(pokemonRepository.listPokemons(limit, offset)).thenReturn(pokemonList)

        viewModel.loadAndFilterPokemonsFromFavoriteList(pokemonListFavorite)

        advanceUntilIdle()

        val favoriteListExpectatio = pokemonList.filter { pokemon ->
            pokemonListFavorite.any{
                it.name == pokemon.name
            }
        }

        Mockito.verify(observer).onChanged(favoriteListExpectatio)
    }

}