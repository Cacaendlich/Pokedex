package com.example.pokedex.presentation.ui.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    enum class LoginState {
        SUCCESS,
        INVALID_CREDENTIALS,
        EMPTY_FIELDS
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun checkNotEmptyCredentials(email: String?, password: String?): Boolean {
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }


    fun isValidTestUser(email: String, password: String): Boolean {
        return email == "teste@teste.com" && password == "1234"
    }

    fun loginIsValid(email: String, password: String){
        if(checkNotEmptyCredentials(email = email, password = password)){
            if (isValidTestUser(email = email, password = password)){
                _loginState.setValue(LoginState.SUCCESS)
            }else{
                _loginState.setValue(LoginState.INVALID_CREDENTIALS)
            }
        }else{
            _loginState.setValue(LoginState.EMPTY_FIELDS)
        }
    }


    fun saveLoginData(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("MySharedPrefs_login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getStoredLoginData(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MySharedPrefs_login", MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun sharedPrefsIsNotEmpty(context: Context): Boolean = getStoredLoginData(context).isNotEmpty() && getStoredLoginData(context) != "null"

}