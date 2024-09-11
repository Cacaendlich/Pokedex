package com.example.pokedex.presenter.ui.battle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.data.model.PokemonType
import com.example.pokedex.data.model.Type
import com.example.pokedex.data.repository.api.PokemonApiRepository
import com.example.pokedex.domain.model.Pokemon
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BattleViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BattleViewModel

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
        viewModel = BattleViewModel(pokemonRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `validar se pokemon n e null`() {
        val result = viewModel.validatePokemon(fakePokemon)

        val exception = assertThrows<NullPointerException> {
            runBlocking {
                viewModel.validatePokemon(null)
            }
        }

        Assert.assertEquals("Attempt to invoke method on a null object reference", exception.message)
        Assert.assertEquals(fakePokemon,result)
    }
}