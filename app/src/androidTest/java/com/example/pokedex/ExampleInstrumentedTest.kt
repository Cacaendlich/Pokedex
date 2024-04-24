package com.example.pokedex

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.database.PokemonDataBase
import com.example.pokedex.data.local.model.PokemonEntity
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PokemonDaoUnitTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var db: PokemonDataBase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDataBase::class.java
        ).build()
        pokemonDao = db.PokemonDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetPokemon() = runBlocking {
        val pokemon = PokemonEntity(1, "Bulbasaur")
        pokemonDao.insertPokemon(pokemon)

        val retrievedPokemon = pokemonDao.getAllPokemons()
        assertTrue(retrievedPokemon.contains(pokemon))
    }

    @Test
    fun deletePokemon() = runBlocking {
        val pokemon = PokemonEntity(1, "Bulbasaur")
        pokemonDao.insertPokemon(pokemon)

        pokemonDao.deletePokemon(pokemon)

        val retrievedPokemon = pokemonDao.getAllPokemons()
        assertFalse(retrievedPokemon.contains(pokemon))
    }

}