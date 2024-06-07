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


    private fun isValidTestUser(email: String, password: String): Boolean {
        return email == "example@example.com" && password == "1234"
    }

    fun loginIsValid(email: String, password: String): String {
        return if(checkNotEmptyCredentials(email = email, password = password)){
            if (isValidTestUser(email = email, password = password)){
                "1"
            } else {
                "2"
            }
        }else{
            "3"
        }
    }

    fun updateLoginState(email: String, password: String) {
        when (loginIsValid(email, password)) {
            "1" -> _loginState.value = LoginState.SUCCESS
            "2" -> _loginState.value = LoginState.INVALID_CREDENTIALS
            "3"-> _loginState.value = LoginState.EMPTY_FIELDS
            else -> _loginState.value = LoginState.EMPTY_FIELDS
        }
    }



    fun saveLoginData(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("MySharedPrefs_login", MODE_PRIVATE)

        val storedEmail = getStoredLoginData(context)

        if (storedEmail == email){
            return
        }

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