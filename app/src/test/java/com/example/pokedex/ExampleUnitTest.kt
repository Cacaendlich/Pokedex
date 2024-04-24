package com.example.pokedex

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
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
    fun deletePokemon1() = runBlocking {
        val pokemon = PokemonEntity(1,"bulbasaur")

        `when`(pokemonDao.getAllPokemons()).thenReturn(emptyList())

        pokemonDao.insertPokemon(pokemon)

        pokemonDao.deletePokemon(pokemon)

        val allPokemons = pokemonDao.getAllPokemons()

        assertFalse(allPokemons.contains(pokemon))
    }
    @Test
    fun deletePokemon2() = runBlocking {
        val pokemon1 = PokemonEntity(1, "bulbasaur")
        val pokemon2 = PokemonEntity(2, "charmander")

        `when`(pokemonDao.getAllPokemons()).thenReturn(listOf(pokemon2))

        pokemonDao.insertPokemon(pokemon1)
        pokemonDao.insertPokemon(pokemon2)

        pokemonDao.deletePokemon(pokemon1)

        val allPokemons = pokemonDao.getAllPokemons()

        assertTrue(allPokemons.contains(pokemon2))
        assertFalse(allPokemons.contains(pokemon1))
    }
    @Test
    fun deletePokemon3() = runBlocking {
        val pokemon1 = PokemonEntity(1, "bulbasaur")
        val pokemon2 = PokemonEntity(2, "charmander")

        `when`(pokemonDao.getAllPokemons()).thenReturn(emptyList())

        pokemonDao.insertPokemon(pokemon1)
        pokemonDao.insertPokemon(pokemon2)

        pokemonDao.deletePokemon(pokemon1)
        pokemonDao.deletePokemon(pokemon2)

        val allPokemons = pokemonDao.getAllPokemons()

        assertFalse(allPokemons.contains(pokemon2))
        assertFalse(allPokemons.contains(pokemon1))
    }
}