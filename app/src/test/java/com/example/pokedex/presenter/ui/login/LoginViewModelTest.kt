package com.example.pokedex.presenter.ui.login

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
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


    @Test
    fun `loginIsValid - deve retornar 1 com credenciais validas`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = "example@example.com"
        val password = "1234"

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "1", result)
    }

    @Test
    fun `loginIsValid - deve retornar 2 com senha invalida`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = "example@example.com"
        val password = "123"

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "2", result)
    }

    @Test
    fun `loginIsValid - deve retornar 2 com e-mail invalido`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = "example.com"
        val password = "1234"

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "2", result)
    }

    @Test
    fun `loginIsValid - deve retornar 3 com e-mail vazio`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = ""
        val password = "1234"

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "3", result)
    }

    @Test
    fun `loginIsValid - deve retornar 3 com senha vazia`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = "example@example.com"
        val password = ""

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "3", result)
    }
    @Test
    fun `loginIsValid - deve retornar 3 com credenciais vazias`() {
        // Create an active task
        // Given: Configurar condições iniciais e dependências para o teste
        val email = ""
        val password = ""

        // Call your function
        // When: Realizar a ação ou método que está sendo testado
        val result = loginViewModel.loginIsValid(email, password)

        // Check the result
        // Then: Verificar os resultados esperados
        assertEquals( "3", result)
    }

    @Test
    fun `checkNotEmptyCredentials - deve retornar false com e-mail nulo`() {
        val email = null
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }

    @Test
    fun `checkNotEmptyCredentials - deve retornar false com senha nula`() {
        val email = "example@example.com"
        val senha = null

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun `checkNotEmptyCredentials - deve retornar true com credenciais validas`() {
        val email = "example@example.com"
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertTrue(result)
    }

    @Test
    fun `checkNotEmptyCredentials - deve retornar false com e-mail vazio`() {
        val email = ""
        val senha = "1234"

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }
    @Test
    fun `checkNotEmptyCredentials - deve retornar false com senha vazia`() {
        val email = "example@example.com"
        val senha = ""

        val result = loginViewModel.checkNotEmptyCredentials(email, senha)

        assertFalse(result)
    }

    @Test
    fun `saveLoginData - deve salvar novo e-mail com SharedPreferences vazio`() {
        // Given: Scenario where `getStoredLoginData(context)` returns an empty string.
        `when`(loginViewModel.getStoredLoginData(context)).thenReturn("")

        // Stubbing for SharedPreferences edit and putString methods.
        `when`(sharedPreferences.edit()).thenReturn(editor)

        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // When: Invoking `loginViewModel.saveLoginData(context, "example@example.com")`.
        loginViewModel.saveLoginData(context, "example@example.com")

        // Then: Expected behavior is to call `putString("email", "example@example.com")` on editor.
        verify(editor).putString("email", "example@example.com")
        // And then I hope you apply the changes.
        verify(editor).apply()
    }

    @Test
    fun `saveLoginData - nao deve salvar e-mail com SharedPreferences nao vazio`() {
        //stubs
        `when`(loginViewModel.getStoredLoginData(context)).thenReturn("example@example.com")

        `when`(sharedPreferences.edit()).thenReturn(editor)

        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        loginViewModel.saveLoginData(context, "example@example.com")

        verify(editor, never()).putString("email", "example@example.com")
        verify(editor, never()).apply()
    }


    @Test
    fun `sharedPrefsIsNotEmpty - deve retornar false com SharedPreferences vazio`() {
        // Teste para verificar se SharedPreferences está vazio

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("")

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

    @Test
    fun `sharedPrefsIsNotEmpty - deve retornar true com SharedPreferences valido`() {
        // Teste para verificar se SharedPreferences não está vazio

        // Configuração do mock de SharedPreferences para retornar um valor específico
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("example@example.com")

        // Chama o método que será testado
        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        // Verifica se o resultado é o esperado
        assertTrue(result)
    }

    @Test
    fun `sharedPrefsIsNotEmpty - deve retornar false com SharedPreferences nulo`() {
        //stub
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)

        val result = loginViewModel.sharedPrefsIsNotEmpty(context)

        assertFalse(result)
    }

}