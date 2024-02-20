package com.example.pokedex.viewmodels

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    fun userIsNotEmpty(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun userIsValid(email: String, password: String): Boolean {
        return email == "teste@email.com" && password == "1234"
    }
}

