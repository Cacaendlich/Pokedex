package com.example.pokedex.presenter.ui.details

import android.graphics.drawable.GradientDrawable
import android.widget.RelativeLayout
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
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
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
    @Mock
    private lateinit var relativeLayout: RelativeLayout

    private val fakePokemon = Pokemon(
        number = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        stats = listOf(),
        type = listOf("grass", "poison"),
        favorite = false
    )

    private val fakePokemon2 = Pokemon(
        number = 4,
        name = "charmander",
        height = 6,
        weight = 85,
        stats = listOf(),
        type = listOf("fire"),
        favorite = false
    )

    private val fakePokemon3 = Pokemon(
        number = 7,
        name = "squirtle",
        height = 5,
        weight = 90,
        stats = listOf(),
        type = listOf("water"),
        favorite = true
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
    fun `live data foi atualizado com sucesso`() = runTest{
        val name = "bulbasaur"
        val expectedPokemon = fakePokemon

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

    @Test
    fun `update background color ta chamando backgroundColor`(){
        val color = -12324

        viewModel.updateBackgroundColor(relativeLayout, color)

        verify(relativeLayout).background = any(GradientDrawable::class.java)
    }

    @Test
    fun `update background color nao chama pq color e -1`(){
        val color = -1

        viewModel.updateBackgroundColor(relativeLayout, color)

        verify(relativeLayout, never()).background = any(GradientDrawable::class.java)
    }

}