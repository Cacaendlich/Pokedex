package com.example.pokedex.presentation.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.ArgumentMatchers.anyInt
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

    @Mock
    private  lateinit var observer: Observer<LoginViewModel.LoginState>

    @Before
    fun setUp() {
        // Método setUp(): Configuração do ambiente de teste

        // Inicialização dos mocks
        MockitoAnnotations.openMocks(this)

        // Configuração do mock do Context para retornar o mock de SharedPreferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)

        loginViewModel.loginState.observeForever(observer)
    }

    @After
    fun tearDown() {
        // Método tearDown(): Limpeza de recursos após cada teste
    }


//    @Test
//    fun getLoginState() {
//    }

    @Test
    fun loginIsValid_SUCCESS() {
        val email = "teste@teste.com"
        val password = "1234"

        loginViewModel.loginIsValid(email, password)

        verify(observer).onChanged(LoginViewModel.LoginState.SUCCESS)

    }

    @Test
    fun checkNotEmptyCredentials_Null() {
        val email = null
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun checkNotEmptyCredentials_NotEmpty() {
        val email = "example@example.com"
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertTrue(result)
    }

    @Test
    fun checkNotEmptyCredentials_Empty() {
        val email = ""
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun checkNotEmptyCredentials_Empty2() {
        val email = "example@example.com"
        val senha = ""

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }

//    @Test
//    fun saveLoginData() {
//        //stubs
//        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("example@example.com")
//
//        `when`(sharedPreferences.edit()).thenReturn(editor)
//
//        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)
//
//        loginViewModel.saveLoginData(context, "example@example.com")
//
//        verify(editor).putString("email", "example@example.com")
//        verify(editor).apply()
//
//    }


    @Test
    fun sharedPrefsIsNotEmpty_IsEmpty() {
        // Teste para verificar se SharedPreferences está vazio

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("")

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty() {
        // Teste para verificar se SharedPreferences não está vazio

        // Configuração do mock de SharedPreferences para retornar um valor específico
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("example@example.com")

        // Chama o método que será testado
        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        // Verifica se o resultado é o esperado
        assertTrue(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty_contextIsNull() {
        //stub
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

}