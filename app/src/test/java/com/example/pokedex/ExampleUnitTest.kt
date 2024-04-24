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
    @Mock ////@Mock Esta é uma anotação que é usada para criar um mock de uma classe ou interface
    lateinit var pokemonDao: PokemonDao

    @Before //@Before Esta é uma anotação que é usada em métodos de teste para indicar que eles devem ser executados antes de cada método de teste na classe.
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test //@Test Esta é uma anotação que indica que este é um método de teste
    fun insertPokemon() = runBlocking { // Aqui estamos definindo o nome do teste e indicando que ele pode ser executado

        val pokemon = PokemonEntity(1,"bulbasaur") // Aqui estamos criando uma instância de PokemonEntity
        // com dados fictícios para simular um Pokémon

        // Agora, estamos configurando o comportamento do mock pokemonDao:
        `when`(pokemonDao.getAllPokemons()).thenReturn(listOf(pokemon))// Quando o método getAllPokemons() for chamado,
        // ele irá retornar uma lista contendo o Pokémon "bulbasaur"

        // Em seguida, chamamos o método insertPokemon() do mock pokemonDao para inserir o Pokémon "bulbasaur" no banco de dados
        pokemonDao.insertPokemon(pokemon)

        // Agora, recuperamos todos os Pokémon do banco de dados
        val allPokemons = pokemonDao.getAllPokemons()

        // Finalmente, estamos verificando se o Pokémon "bulbasaur" está presente na lista de todos os Pokémon recuperados do banco de dados
        assertTrue(allPokemons.contains(pokemon)) // Isso verifica se a lista de todos os Pokémon contém o Pokémon "bulbasaur"
    }
}