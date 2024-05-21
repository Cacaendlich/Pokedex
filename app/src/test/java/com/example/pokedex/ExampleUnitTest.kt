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

        `when`(pokemonDao.getAllPokemonsFavorites()).thenReturn(listOf(pokemon))

        pokemonDao.insertPokemonFavorite(pokemon)

        val allPokemons = pokemonDao.getAllPokemonsFavorites()

        assertTrue(allPokemons.contains(pokemon))
    }

    @Test
    fun deletePokemon1() = runBlocking {
        val pokemon = PokemonEntity(1,"bulbasaur")

        `when`(pokemonDao.getAllPokemonsFavorites()).thenReturn(emptyList())

        pokemonDao.insertPokemonFavorite(pokemon)

        pokemonDao.deletePokemonFavorite(pokemon)

        val allPokemons = pokemonDao.getAllPokemonsFavorites()

        assertFalse(allPokemons.contains(pokemon))
    }
    @Test
    fun deletePokemon2() = runBlocking {
        val pokemon1 = PokemonEntity(1, "bulbasaur")
        val pokemon2 = PokemonEntity(2, "charmander")

        `when`(pokemonDao.getAllPokemonsFavorites()).thenReturn(listOf(pokemon2))

        pokemonDao.insertPokemonFavorite(pokemon1)
        pokemonDao.insertPokemonFavorite(pokemon2)

        pokemonDao.deletePokemonFavorite(pokemon1)

        val allPokemons = pokemonDao.getAllPokemonsFavorites()

        assertTrue(allPokemons.contains(pokemon2))
        assertFalse(allPokemons.contains(pokemon1))
    }
    @Test
    fun deletePokemon3() = runBlocking {
        val pokemon1 = PokemonEntity(1, "bulbasaur")
        val pokemon2 = PokemonEntity(2, "charmander")

        `when`(pokemonDao.getAllPokemonsFavorites()).thenReturn(emptyList())

        pokemonDao.insertPokemonFavorite(pokemon1)
        pokemonDao.insertPokemonFavorite(pokemon2)

        pokemonDao.deletePokemonFavorite(pokemon1)
        pokemonDao.deletePokemonFavorite(pokemon2)

        val allPokemons = pokemonDao.getAllPokemonsFavorites()

        assertFalse(allPokemons.contains(pokemon2))
        assertFalse(allPokemons.contains(pokemon1))
    }
}
