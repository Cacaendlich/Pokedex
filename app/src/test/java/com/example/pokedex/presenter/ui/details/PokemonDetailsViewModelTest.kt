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
    fun `deve atualizar o LiveData com o Pokemon correto quando carregado com sucesso`() = runTest{
        val name = "bulbasaur"
        val expectedPokemon = fakePokemon

        `when`(pokemonRepository.getPokemons(anyString())).thenReturn(expectedPokemon)

        viewModel.loadPokemon(name)

        Assert.assertEquals(expectedPokemon, viewModel.pokemonLiveData.value)
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

    @Test
    fun `deve chamar o metodo de atualizacao da cor de fundo quando a cor for valida`(){
        val color = -12324

        viewModel.updateBackgroundColor(relativeLayout, color)

        verify(relativeLayout).background = any(GradientDrawable::class.java)
    }

    @Test
    fun `nao deve chamar o metodo de atualizacao da cor de fundo quando a cor for invalida`(){
        val color = -1

        viewModel.updateBackgroundColor(relativeLayout, color)

        verify(relativeLayout, never()).background = any(GradientDrawable::class.java)
    }


}