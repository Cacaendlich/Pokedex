package com.example.pokedex.presenter.ui.pokemonsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    private lateinit var pokemonLocalRepository: PokemonLocalRepository

    @Mock
    private lateinit var observerFav: Observer<List<PokemonEntity>>
    @Mock
    private lateinit var observerPokemonFavoriteState: Observer<List<Pokemon>>

    private val grassType = PokemonType(slot = 1, type = Type(name = "grass"))
    private val poisonType = PokemonType(slot = 2, type = Type(name = "poison"))
    private val fireType = PokemonType(slot = 2, type = Type(name = "fire"))
    private val waterType = PokemonType(slot = 2, type = Type(name = "water"))

    private val fakePokemon = Pokemon(
        number = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        stats = listOf(),
        type = listOf(grassType,poisonType),
        favorite = false
    )

    private val fakePokemonFav = PokemonEntity(
        pokemonId = 1,
        name = "bulbasaur",
    )

    private val fakePokemon2 = Pokemon(
        number = 4,
        name = "charmander",
        height = 6,
        weight = 85,
        stats = listOf(),
        type = listOf(fireType),
        favorite = false
    )

    private val fakePokemon2Fav = PokemonEntity(
        pokemonId = 4,
        name = "charmander",
    )

    private val fakePokemon3 = Pokemon(
        number = 7,
        name = "squirtle",
        height = 5,
        weight = 90,
        stats = listOf(),
        type = listOf(waterType),
        favorite = true
    )

    private val fakePokemon3Fav = PokemonEntity(
        pokemonId = 7,
        name = "squirtle",
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonsListViewModel(pokemonRepository, pokemonLocalRepository)
        viewModel.favoriteList.observeForever(observerFav)
        viewModel.pokemonsFavoriteState.observeForever(observerPokemonFavoriteState)
    }

    @After
    fun tearDown() {
        viewModel.favoriteList.removeObserver(observerFav)
        viewModel.pokemonsFavoriteState.removeObserver(observerPokemonFavoriteState)
    }

    @Test
    fun `test initial loading of pokemons successfully`() = runTest {
        val listMock = listOf(
            fakePokemon,
            fakePokemon2,
            fakePokemon3,
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(listMock)

        viewModel.loadInitialPokemons()

        Assert.assertEquals(listMock, viewModel.pokemonsAllState.first())
    }
    @Test
    fun `test initial load of pokemons emptyList`() = runTest {
        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(emptyList())

        viewModel.loadInitialPokemons()

        Assert.assertEquals(emptyList<Pokemon>(), viewModel.pokemonsAllState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `teste carregamento de  mais pokemons com sucesso`() = runTest {
        val initialList = listOf(
            fakePokemon,
            fakePokemon2
        )
        val additionalItens = listOf(
            fakePokemon3
        )
        val expectedUpdatedList = initialList + additionalItens

        `when`(pokemonRepository.listPokemons(20, 0)).thenReturn(initialList)
        `when`(pokemonRepository.listPokemons(20, initialList.size)).thenReturn(additionalItens)

        viewModel.loadInitialPokemons()
        viewModel.loadMorePokemons()

        advanceUntilIdle()
        //avançar o tempo até que todas as coroutines pendentes tenham concluído a execução

        Assert.assertEquals(expectedUpdatedList, viewModel.pokemonsAllState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadFavorites - teste de carregamento da lista de favoritos no Room`() = runTest{
        val allPokemonMock = listOf(
            fakePokemonFav,
            fakePokemon3Fav
        )

        `when`(pokemonLocalRepository.getAllPokemons()).thenReturn(allPokemonMock)

        viewModel.loadFavorites()

        advanceUntilIdle()

        Mockito.verify(observerFav).onChanged(allPokemonMock)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAndFilterPokemonsFromFavoriteList - teste de carregamento da lista de favoritos do tipo Pokemon`() = runTest{
        val allPokemonMock = listOf(
            fakePokemon,
            fakePokemon2,
            fakePokemon3
        )

        val allPokemonFavMock = listOf(
            fakePokemonFav,
            fakePokemon2Fav
        )

        val expectationList = listOf(
            fakePokemon,
            fakePokemon2,
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(allPokemonMock)

        viewModel.loadAndFilterPokemonsFromFavoriteList(allPokemonFavMock)

        advanceUntilIdle()

        Mockito.verify(observerPokemonFavoriteState).onChanged(expectationList)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAndFilterPokemonsFromFavoriteList - teste de carregamento da lista de favoritos do tipo Pokemon vazia`() = runTest{
        val allPokemonMock = listOf(
            fakePokemon,
            fakePokemon2,
            fakePokemon3
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(allPokemonMock)

        viewModel.loadAndFilterPokemonsFromFavoriteList(emptyList())

        advanceUntilIdle()

        Mockito.verify(observerPokemonFavoriteState).onChanged(emptyList())

    }

}