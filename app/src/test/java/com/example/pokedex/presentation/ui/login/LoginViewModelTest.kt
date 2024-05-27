package com.example.pokedex.presentation.ui.login

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.junit.Before

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel //instância da classe LoginViewModel

    @Mock
    private lateinit var context: Context //Aqui, @Mock é uma anotação do Mockito
    // que indica que a variável context será um objeto simulado (mock) da classe Context.


    private lateinit var sharedPreferences: SharedPreferences //para simular um objeto SharedPreferences

    @Before
    fun setUp() {
        //Este método é executado antes de cada teste (@Test).
        // Aqui, estamos configurando o ambient de teste.

        MockitoAnnotations.openMocks(this)

        context = mock(Context::class.java) //Criamos um objeto simulado (mock) da classe Context.
        sharedPreferences = mockSharedPreferences()
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        //Configuramos o comportamento do mock do Context.
        // Estamos dizendo que quando o método getSharedPreferences() é chamado com qualquer String e qualquer Int,
        // ele deve retornar o objeto simulado sharedPreferences.

        loginViewModel = LoginViewModel() //Criamos uma instância da classe LoginViewModel.
    }

    @After
    //Este método é executado após cada teste.
    // Aqui, você pode limpar recursos ou redefinir o estado, se necessário.
    fun tearDown() {
    }


    private fun mockSharedPreferences(): SharedPreferences {
        // Esta é uma função auxiliar que cria um objeto simulado de SharedPreferences
        // e configura seu comportamento.
        val  sharedPreferences = mock(SharedPreferences::class.java)

        `when`(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn("example@example.com")

        return sharedPreferences
    }

//    @Test
//    fun getLoginState() {
//    }
//
//    @Test
//    fun loginIsValid() {
//    }
//
//    @Test
//    fun saveLoginData() {
//    }
//
//    @Test
//    fun getStoredLoginData() {
//    }

    @org.junit.Test
    fun sharedPrefsIsNotEmpty() {
        // Define o comportamento esperado do objeto SharedPreferences simulado
        `when`(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn("example@example.com")

        // Chama o método que será testado
        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        // Verifica se o resultado é o esperado
        Assertions.assertTrue(result)
    }

}