package com.example.pokedex.presenter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.presenter.ui.login.ui.theme.PokedexTheme
import com.example.pokedex.presenter.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        if(viewModel.sharedPrefsIsNotEmpty(this)){
            goToMainActivity()
            return
        } else{
            window.statusBarColor = getColor(R.color.red)

        }

        setContent {
            PokedexTheme {
                val email by viewModel.email.collectAsState()
                val password by viewModel.password.collectAsState()
                LoginScreen(
                    email = email,
                    password = password,
                    onEmailChanged = { viewModel.updateEmail(it)},
                    onPasswordChanged = { viewModel.updatePassword(it)},
                    onLoginClick = { viewModel.updateLoginState(email, password) }
                )
            }
        }


        viewModel.loginState.observeForever{ state ->
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> {
                    val email = viewModel.email.value
                    viewModel.saveLoginData(this, email)
                    goToMainActivity()
                }

                LoginViewModel.LoginState.INVALID_CREDENTIALS -> showMessage(
                    this,
                    "Invalid credentials."
                )

                LoginViewModel.LoginState.EMPTY_FIELDS -> showMessage(
                    this,
                    "Please enter an email and password."
                )

                null -> Log.d("LoginActivity", "LoginState is null - No action taken")
            }
        }

    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}