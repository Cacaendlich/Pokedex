package com.example.pokedex.presenter.ui.battle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.first
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class BattleViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BattleViewModel

    @Mock
    private lateinit var pokemonApiRepository: PokemonApiRepository

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
        viewModel = BattleViewModel(pokemonApiRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `validatePokemon - deve lancar excecao ao validar se pokemon e null`() {
        val result = viewModel.validatePokemon(fakePokemon)

        val exception = assertThrows<NullPointerException> {
            runBlocking {
                viewModel.validatePokemon(null)
            }
        }

        Assert.assertEquals("Attempt to invoke method on a null object reference", exception.message)
        Assert.assertEquals(fakePokemon,result)
    }

    @Test
    fun `loadPokemon1 - deve lancar excecao ao tentar carregar pokemon com nome vazio`() = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            runBlocking {
                viewModel.loadPokemon1("")
            }
        }

        Assert.assertEquals("Nome do Pokémon está vazio", exception.message)
    }

    @Test
    fun `loadPokemon2 - deve lancar excecao ao tentar carregar pokemon com nome vazio`() = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            runBlocking {
                viewModel.loadPokemon2("")
            }
        }

        Assert.assertEquals("Nome do Pokémon está vazio", exception.message)
    }

    @Test
    fun `loadPokemon1 - deve atualizar pokemon1StateFlow corretamente`() = runTest {
        `when`(pokemonApiRepository.getPokemons(anyString())).thenReturn(fakePokemon)

        viewModel.loadPokemon1(fakePokemon.name)

        Assert.assertEquals(fakePokemon,viewModel.pokemon1StateFlow.first())
    }

    @Test
    fun `loadPokemon2 - deve atualizar pokemon2StateFlow corretamente`() = runTest {
        `when`(pokemonApiRepository.getPokemons(anyString())).thenReturn(fakePokemon)

        viewModel.loadPokemon2(fakePokemon.name)

        Assert.assertEquals(fakePokemon,viewModel.pokemon2StateFlow.first())
    }
}