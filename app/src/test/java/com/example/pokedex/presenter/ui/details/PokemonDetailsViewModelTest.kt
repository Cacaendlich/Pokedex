package com.example.pokedex.presenter.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PokemonDetailsViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PokemonDetailsViewModel

    @Mock
    private lateinit var observer: Observer<Pokemon?>
    @Mock
    private lateinit var pokemonRepository: PokemonApiRepository

    private val grassType = PokemonType(slot = 1, type = Type(name = "grass"))
    private val poisonType = PokemonType(slot = 2, type = Type(name = "poison"))

    private val fakePokemon = Pokemon(
        number = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        stats = listOf(),
        type = listOf(grassType, poisonType),
        favorite = false
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonDetailsViewModel(pokemonRepository)
        viewModel.pokemonLiveData.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.pokemonLiveData.removeObserver(observer)
    }

    @Test
    fun `deve atualizar o LiveData com o Pokemon correto quando carregado com sucesso e chamar o repository com o nome correto do Pokemon`() = runTest{
        val name = "bulbasaur"
        val expectedPokemon = fakePokemon

        `when`(pokemonRepository.getPokemons(anyString())).thenReturn(expectedPokemon)

        viewModel.loadPokemon(name)

        Assert.assertEquals(expectedPokemon, viewModel.pokemonLiveData.value)
        verify(pokemonRepository).getPokemons(name)

    }
    @Test
    fun `deve lancar uma excecao quando tentar carregar um Pokemon com nome vazio`() = runTest{
        val name = ""

        val exception = assertThrows<IllegalArgumentException> {
            runBlocking {
                viewModel.loadPokemon(name)

            }
        }
        Assert.assertEquals("Nome do Pokémon está vazio", exception.message)
    }
}