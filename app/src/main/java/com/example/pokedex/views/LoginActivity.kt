package com.example.pokedex.views

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.databinding.ActivityFormLoginBinding
import com.example.pokedex.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        emailEditText = binding.editEmail
        passwordEditText = binding.editPassword

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        var emailNoShared=  false
        viewModel.sharedPrefsIsNotEmpty(this)

        viewModel.loginSharedPrefsState.observe(this) { state ->
            when (state) {
                LoginViewModel.LoginSharedPrefsState.HAS_LOGIN_DATA -> {
                    val emailSave = viewModel.getStoredLoginData(this)
                    emailEditText.setText((emailSave))
                }
                LoginViewModel.LoginSharedPrefsState.NO_LOGIN_DATA -> emailNoShared = true
                null -> showMessage(this,"ERROR")
            }
        }

        binding.buttonLogin.setOnClickListener {
            viewModel.loginIsValid(email = emailEditText.text.toString(), password = passwordEditText.text.toString(), context = this)

            if(emailNoShared) {
                viewModel.saveLoginData(this, emailEditText)
            }
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> showMessage(this,"LOGIN SUCCESS!")
                LoginViewModel.LoginState.INVALID_CREDENTIALS -> showMessage(this,"Invalid credentials.")
                LoginViewModel.LoginState.EMPTY_FIELDS -> showMessage(this,"Please enter an email and password.")
                null -> showMessage(this,"ERROR")
            }
        }
    }
    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}