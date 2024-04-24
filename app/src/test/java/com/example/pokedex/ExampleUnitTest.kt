package com.example.pokedex

import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.model.PokemonEntity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PokemonDaoUnitTest {
    @Mock
    lateinit var pokemonDao: PokemonDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun insertPokemon() = runBlocking {

        val pokemon = PokemonEntity(1,"bulbasaur")

        `when`(pokemonDao.getAllPokemons()).thenReturn(listOf(pokemon))

        pokemonDao.insertPokemon(pokemon)

        val allPokemons = pokemonDao.getAllPokemons()

        assertTrue(allPokemons.contains(pokemon))
    }

    @Test
    fun deletePokemon() = runBlocking {
        val pokemon = PokemonEntity(1,"bulbasaur")

        `when`(pokemonDao.getAllPokemons()).thenReturn(emptyList())

        pokemonDao.insertPokemon(pokemon)

        pokemonDao.deletePokemon(pokemon)

        val allPokemons = pokemonDao.getAllPokemons()

        assertFalse(allPokemons.contains(pokemon))
    }

}