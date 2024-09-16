package com.example.pokedex.presenter.ui.pokemonsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.data.repository.local.PokemonLocalRepository
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.presenter.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

class MainViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var pokemonRepository: PokemonApiRepository
    @Mock
    private lateinit var pokemonLocalRepository: PokemonLocalRepository

    @Mock
    private lateinit var observerFavoriteList: Observer<List<PokemonEntity>>

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

    private val fakePokemon2 = Pokemon(
        number = 4,
        name = "charmander",
        height = 6,
        weight = 85,
        stats = listOf(),
        type = listOf(fireType),
        favorite = false
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
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(pokemonRepository, pokemonLocalRepository)
        viewModel.favoriteList.observeForever(observerFavoriteList)
    }

    @After
    fun tearDown() {
        viewModel.favoriteList.removeObserver(observerFavoriteList)
    }

    @Test
    fun `loadInitialPokemons - deve carregar pokemons com sucesso`() = runTest {
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
    fun `loadInitialPokemons - deve carregar uma lista vazia de pokemons`() = runTest {
        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(emptyList())

        viewModel.loadInitialPokemons()

        Assert.assertEquals(emptyList<Pokemon>(), viewModel.pokemonsAllState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadMorePokemons - deve carregar mais pokemons a partir do ultimo carregamento`() = runTest {
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
    fun `loadFavorites - deve carregar a lista de favoritos com sucesso`() = runTest{
        val allPokemonsFav = listOf(
            PokemonEntity(pokemonId = 1, name = "bulbasaur"),
            PokemonEntity(pokemonId = 4, name = "charmander"),
            PokemonEntity(pokemonId = 7, name = "squirtle")
        )

        `when`(pokemonLocalRepository.getAllPokemons()).thenReturn(allPokemonsFav)

        viewModel.loadFavorites()

        advanceUntilIdle()

        Mockito.verify(observerFavoriteList).onChanged(allPokemonsFav)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAndFilterPokemonsFromFavoriteList - deve filtrar e carregar pokemons favoritos do tipo Pokemon`() = runBlocking{
        val loadPokemonsMock = listOf(
            fakePokemon,
            fakePokemon2,
            fakePokemon3,
        )
        val favoriteListMock = listOf(
            PokemonEntity(pokemonId = 1, name = "bulbasaur"),
            PokemonEntity(pokemonId = 7, name = "squirtle")
        )

        val expectationList = listOf(
            fakePokemon,
            fakePokemon3,
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(loadPokemonsMock)

        viewModel.loadAndFilterPokemonsFromFavoriteList(favoriteListMock).join()

        Assert.assertEquals(expectationList, viewModel.pokemonsFavoriteState.first())

    }

    @Test
    fun `toggleFavoritesView - deve alternar corretamente entre exibir e ocultar favoritos`() = runTest{
        viewModel.toggleFavoritesView(true)
        Assert.assertEquals(true, viewModel.isDisplayingFavorites.first())

        viewModel.toggleFavoritesView(false)
        Assert.assertEquals(false, viewModel.isDisplayingFavorites.first())

    }

    @Test
    fun `selectPokemons - deve adicionar pokemon selecionado`() = runTest{

        viewModel.selectPokemons(fakePokemon)

        Assert.assertTrue(viewModel.onSelectPokemon.contains(fakePokemon))

    }

    @Test
    fun `selectPokemons - deve remover pokemon ja selecionado`() = runTest{

        viewModel.selectPokemons(fakePokemon)

        Assert.assertTrue(viewModel.onSelectPokemon.contains(fakePokemon))

        viewModel.selectPokemons(fakePokemon)

        Assert.assertFalse(viewModel.onSelectPokemon.contains(fakePokemon))

    }

    @Test
    fun `selectPokemons - deve conter dois pokemons selecionados`() = runTest{

        viewModel.selectPokemons(fakePokemon)

        viewModel.selectPokemons(fakePokemon2)

        Assert.assertTrue(viewModel.onSelectPokemon.size == 2)

    }

    @Test
    fun `removeAllBattle - deve remover todos os pokemons selecionados da batalha`(){
        viewModel.selectPokemons(fakePokemon)

        viewModel.selectPokemons(fakePokemon2)

        Assert.assertTrue(viewModel.onSelectPokemon.contains(fakePokemon))
        Assert.assertTrue(viewModel.onSelectPokemon.contains(fakePokemon2))

        viewModel.removeAllBattle()

        Assert.assertFalse(viewModel.onSelectPokemon.contains(fakePokemon))
        Assert.assertFalse(viewModel.onSelectPokemon.contains(fakePokemon2))
    }

}