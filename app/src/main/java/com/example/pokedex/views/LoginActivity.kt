package com.example.pokedex.views


import android.os.Bundle
import android.widget.EditText
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

        if(viewModel.sharedPrefsIsNotEmpty(this)){
            val emailConsult = viewModel.getStoredLoginData(this)
            emailEditText.setText(emailConsult)
        }

        binding.buttonLogin.setOnClickListener {
            viewModel.loginIsValid(email = emailEditText.text.toString(), password = passwordEditText.text.toString(), context = this)
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> viewModel.showMessage(this,"LOGIN SUCCESS!")
                LoginViewModel.LoginState.INVALID_CREDENTIALS -> viewModel.showMessage(this,"Invalid credentials.")
                LoginViewModel.LoginState.EMPTY_FIELDS -> viewModel.showMessage(this,"Please enter an email and password.")
                null -> viewModel.showMessage(this,"ERROR")
            }
        }
    }
}