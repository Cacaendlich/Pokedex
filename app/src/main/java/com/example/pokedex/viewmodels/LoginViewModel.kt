package com.example.pokedex.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private fun checkNotEmptyCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun isValidTestUser(email: String, password: String): Boolean {
        return email == "teste@email.com" && password == "1234"
    }

    private fun msgSuccess(context: Context){
        return Toast.makeText(context, "LOGIN SUCCESS", Toast.LENGTH_SHORT).show()
    }

    private fun msgErrorEmailOrPassword(context: Context){
        Toast.makeText(context, "Your email or password is incorrect. Please try again.", Toast.LENGTH_SHORT).show()
    }

    private fun msgErrorIsEmpty(context: Context){
        Toast.makeText(context, "Please enter an email and password", Toast.LENGTH_SHORT).show()
    }

    fun loginIsValid(email: String, password: String, context: Context){
        if(checkNotEmptyCredentials(email = email, password = password)){
            if (isValidTestUser(email = email, password = password)){
                msgSuccess(context = context)
            }else{
                msgErrorEmailOrPassword(context = context)
            }
        }else{
            msgErrorIsEmpty(context = context)
        }
    }
}


