package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.databinding.ActivityFormLoginBinding
import com.example.pokedex.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize a ViewModel thousand ViewModelProvider
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if(viewModel.userIsNotEmpty(email = email, password = password)){
                if (viewModel.userIsValid(email = email, password = password)){
                    Toast.makeText(this, "Email: $email Password: $password", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "Your email or password is incorrect. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}