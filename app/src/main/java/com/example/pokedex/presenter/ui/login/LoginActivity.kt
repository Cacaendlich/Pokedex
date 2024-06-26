package com.example.pokedex.presenter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityFormLoginBinding
import com.example.pokedex.presenter.ui.main.MainActivity

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
            goToMainActivity()
            return
        }else {
            emailEditText.requestFocus()
            val emailConsult = viewModel.getStoredLoginData(this)
            emailEditText.setText(emailConsult)
        }

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performLogin()
                true
            } else {
                false
            }
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> {
                    viewModel.saveLoginData(this, emailEditText.text.toString())
                    goToMainActivity()
                }
                LoginViewModel.LoginState.INVALID_CREDENTIALS -> showMessage(this,"Invalid credentials.")
                LoginViewModel.LoginState.EMPTY_FIELDS -> showMessage(this,"Please enter an email and password.")
                null -> showMessage(this,"ERROR")
            }
        }

        window.statusBarColor = getColor(R.color.read)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun performLogin() {
        viewModel.updateLoginState(
            email = emailEditText.text.toString(),
            password = passwordEditText.text.toString()
        )
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}