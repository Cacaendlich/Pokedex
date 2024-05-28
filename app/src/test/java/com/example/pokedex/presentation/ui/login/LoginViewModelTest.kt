package com.example.pokedex.presentation.ui.login

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginViewModelTest {

    private  var loginViewModel: LoginViewModel = LoginViewModel() // Instância da classe LoginViewModel

    @Mock
    private lateinit var context: Context // Mock do Context para simular o ambiente Android

    @Mock
    private lateinit var sharedPreferences: SharedPreferences // Mock do SharedPreferences para simular o armazenamento de dados

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        // Método setUp(): Configuração do ambiente de teste

        // Inicialização dos mocks
        MockitoAnnotations.openMocks(this)

    }

    @After
    fun tearDown() {
        // Método tearDown(): Limpeza de recursos após cada teste
    }


//    @Test
//    fun getLoginState() {
//    }
//
//    @Test
//    fun loginIsValid() {
//    }

    @Test
    fun saveLoginData() {
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(eq("example@example.com"))

        `when`(sharedPreferences.edit()).thenReturn(editor)

        loginViewModel.saveLoginData(context, "example@example.com")

        verify(editor).putString(anyString(), eq("example@example.com"))
        verify(editor).apply()

    }

    @Test
    fun sharedPrefsIsNotEmpty_IsEmpty() {
        // Teste para verificar se SharedPreferences está vazio

        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("")

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        Assertions.assertFalse(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty() {
        // Teste para verificar se SharedPreferences não está vazio

        // Configuração do mock do Context para retornar o mock de SharedPreferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)

        // Configuração do mock de SharedPreferences para retornar um valor específico
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("example@example.com")

        // Chama o método que será testado
        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        // Verifica se o resultado é o esperado
        Assertions.assertTrue(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty_contextIsNull() {
        // Teste para verificar se o método lida  com um contexto null

        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        Assertions.assertFalse(result)
    }




}