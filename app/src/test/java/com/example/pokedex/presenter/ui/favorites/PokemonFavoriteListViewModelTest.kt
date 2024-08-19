package com.example.pokedex.presenter.ui.favorites

import android.view.View
import android.widget.ProgressBar
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.local.model.PokemonEntity
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
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
import org.mockito.Mockito.anyInt
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
    private lateinit var progressBar: ProgressBar


    @Mock
    private lateinit var observer: Observer<List<Pokemon?>>
    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean>
    @Mock
    private lateinit var favoriteListObserver: Observer<List<PokemonEntity?>>

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
         val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasaur"),
            PokemonEntity(4, "charmander")
        )

        val result1 = viewModel.isFavorite(pokemonListFavorite, fakePokemon)
        val result2 = viewModel.isFavorite(pokemonListFavorite, fakePokemon2)

        Assert.assertTrue(result1)
        Assert.assertTrue(result2)
    }

    @Test
    fun `isFavorite NAO achando pokemon na lista de favoritos`() {
        val pokemon1 = fakePokemon
        val pokemon2 = fakePokemon3

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
        val pokemon1 = fakePokemon
        val pokemonListFavorite = emptyList<PokemonEntity>()
        val pokemonFavoritado = PokemonEntity(pokemon1.number, pokemon1.name)

        viewModel.updateFavoritesList(pokemon1, pokemonListFavorite)

        Mockito.verify(pokemonLocalRepository).addFavorite(pokemonFavoritado)
    }

    @Test
    fun `updateFavoriteList quando o pokemon esta na lista de favoritos`() = runTest {
        val pokemon1 = fakePokemon3
        val pokemonListFavorite = listOf(
            PokemonEntity(7, "squirtle")
        )

        viewModel.updateFavoritesList(pokemon1, pokemonListFavorite)

        Mockito.verify(pokemonLocalRepository).deleteFavorite(pokemon1.number)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadFavorites com sucesso`() = runTest{
        val pokemonListFavorite = listOf(
            PokemonEntity(1, "bulbasaur"),
            PokemonEntity(2, "ivysaur")
        )

        `when`(pokemonLocalRepository.getAllPokemons()).thenReturn(pokemonListFavorite)

        viewModel.loadFavorites()
        Mockito.verify(isLoadingObserver).onChanged(true)

        advanceUntilIdle()

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
            fakePokemon,
            fakePokemon2,
            fakePokemon3,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `quando a lista de favoritos esta vazia, o icone de ninho vazio e a mensagem sao exibidos`() = runTest{
//        Given a lista de pokemons favoritos está vazia
        val listEmpty = emptyList<PokemonEntity>()
//        Given a lista de pokemons nao está vazia
        val pokemonList = listOf(
            fakePokemon,
            fakePokemon2,
            fakePokemon3,
        )

        `when`(pokemonRepository.listPokemons(anyInt(), anyInt())).thenReturn(pokemonList)

        viewModel.loadAndFilterPokemonsFromFavoriteList(listEmpty)

        advanceUntilIdle()

        Mockito.verify(observer).onChanged(emptyList())
        Assert.assertEquals(View.VISIBLE, progressBar.visibility)

//                When o usuário clica para ver a lista de favoritos
//                Then o carregamento desaparece
//                And um ícone de ninho vazio aparece
//        And uma mensagem dizendo "Você ainda não favoritou nenhum Pokémon
    }

}