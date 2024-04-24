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
    @Mock // Referência simulada para PokemonDao
    lateinit var pokemonDao: PokemonDao

    @Before // Configuração pré-teste
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test //Aqui é o teste mesmo
    fun insertPokemon() = runBlocking { //definindo o nome do teste e indicando que ele pode ser executado

        val pokemon = PokemonEntity(1,"bulbasaur") // instânciei PokemonEntity

        //configurando o comportamento esperado do mock pokemonDao
        `when`(pokemonDao.getAllPokemons()).thenReturn(listOf(pokemon))
        // Quando o método getAllPokemons() for chamado, ele deveria retornar uma lista contendo o Pokémon "bulbasaur"

        // Em seguida, chamamos o método insertPokemon() do mock pokemonDao para inserir o Pokémon "bulbasaur" no banco de dados
        pokemonDao.insertPokemon(pokemon)

        // Agora, allPokemons recebe a lógica de buscar todos os Pokémon do banco de dados
        val allPokemons = pokemonDao.getAllPokemons()

        // Finalmente, estamos verificando se o Pokémon "bulbasaur" está presente na lista de todos os Pokémon recuperados do banco de dados
        assertTrue(allPokemons.contains(pokemon)) // Isso verifica se a lista de todos os Pokémon contém o Pokémon "bulbasaur"
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