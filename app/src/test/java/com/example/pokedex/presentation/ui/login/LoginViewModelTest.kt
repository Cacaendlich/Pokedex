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
import org.mockito.Mockito.never
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

        // Configuração do mock do Context para retornar o mock de SharedPreferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
    }

    @After
    fun tearDown() {
        // Método tearDown(): Limpeza de recursos após cada teste
    }


//    @Test
//    fun loginIsValid_withValidCredentials_setsSuccessState() {
//        val email = "teste@teste.com"
//        val password = "1234"
//
//        loginViewModel.loginIsValid(email, password)
//    }

    @Test
    fun checkNotEmptyCredentials_withNullEmail_returnsFalse() {
        val email = null
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }

    @Test
    fun checkNotEmptyCredentials_withNullPassword_returnsFalse() {
        val email = "example@example.com"
        val senha = null

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun checkNotEmptyCredentials_withValidCredentials_returnsTrue() {
        val email = "example@example.com"
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertTrue(result)
    }

    @Test
    fun checkNotEmptyCredentials_withEmptyEmail_returnsEmpty() {
        val email = ""
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun checkNotEmptyCredentials_withEmptyPassword_returnsEmpty() {
        val email = "example@example.com"
        val senha = ""

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }

    @Test
    fun saveLoginData_withEmptySharedPreferences_savesNewEmail() {
        //stubs
        `when`(loginViewModel.getStoredLoginData(context)).thenReturn("")

        `when`(sharedPreferences.edit()).thenReturn(editor)

        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        loginViewModel.saveLoginData(context, "example@example.com")

        verify(editor).putString("email", "example@example.com")
        verify(editor).apply()
    }

    @Test
    fun saveLoginData_withNotEmptySharedPreferences_NotSavesEmail() {
        //stubs
        `when`(loginViewModel.getStoredLoginData(context)).thenReturn("example@example.com")

        `when`(sharedPreferences.edit()).thenReturn(editor)

        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        loginViewModel.saveLoginData(context, "example@example.com")

        verify(editor, never()).putString("email", "example@example.com")
        verify(editor, never()).apply()
    }


    @Test
    fun sharedPrefsIsNotEmpty_withEmptySharedPreferences_returnsFalse() {
        // Teste para verificar se SharedPreferences está vazio

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("")

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty_withSharedPreferencesValid_returnsTrue() {
        // Teste para verificar se SharedPreferences não está vazio

        // Configuração do mock de SharedPreferences para retornar um valor específico
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("example@example.com")

        // Chama o método que será testado
        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        // Verifica se o resultado é o esperado
        assertTrue(result)
    }

    @Test
    fun sharedPrefsIsNotEmpty_withNullSharedPreferences_returnsFalse() {
        //stub
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

}