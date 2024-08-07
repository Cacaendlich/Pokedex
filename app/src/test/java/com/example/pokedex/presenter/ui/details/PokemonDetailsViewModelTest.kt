package com.example.pokedex.presenter.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
    fun `live data foi atualizado com sucesso`() = runTest{
        val name = "bulbasaur"
        val expectedPokemon = Pokemon(1, name)

        `when`(pokemonRepository.getPokemons(anyString())).thenReturn(expectedPokemon)

        viewModel.loadPokemon(name)

        Assert.assertEquals(expectedPokemon, viewModel.pokemonLiveData.value)
    }
    @Test
    fun `live data foi atualizado com uma string vazia`() = runTest{
        val name = ""

        val exception = assertThrows<IllegalArgumentException> {
            runBlocking {
                viewModel.loadPokemon(name)

            }
        }
        Assert.assertEquals("Nome do Pokémon está vazio", exception.message)
    }
}