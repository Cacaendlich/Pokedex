package com.example.pokedex.viewmodels

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    enum class LoginState {
        SUCCESS,
        INVALID_CREDENTIALS,
        EMPTY_FIELDS
    }
    enum class LoginSharedPrefsState {
        HAS_LOGIN_DATA,
        NO_LOGIN_DATA
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _loginSharedPrefsState = MutableLiveData<LoginSharedPrefsState>()
    val loginSharedPrefsState: LiveData<LoginSharedPrefsState> = _loginSharedPrefsState

    private fun checkNotEmptyCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun isValidTestUser(email: String, password: String): Boolean {
        return email == "teste@email.com" && password == "1234"
    }

    fun loginIsValid(email: String, password: String, context: Context){
        if(checkNotEmptyCredentials(email = email, password = password)){
            if (isValidTestUser(email = email, password = password)){
                _loginState.setValue(LoginState.SUCCESS)
            }else{
                _loginState.setValue(LoginState.INVALID_CREDENTIALS)
            }
        }else {
            _loginState.setValue(LoginState.EMPTY_FIELDS)
        }
    }

    fun saveLoginData(context: Context, email: EditText) {
        val sharedPreferences = context.getSharedPreferences("MySharedPrefs_login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email.toString())
        editor.apply()
    }

    fun getStoredLoginData(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MySharedPrefs_login", MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun sharedPrefsIsNotEmpty(context: Context) {
        if(getStoredLoginData(context).isNotEmpty()){
            _loginSharedPrefsState.setValue(LoginSharedPrefsState.HAS_LOGIN_DATA)
        }else{
            _loginSharedPrefsState.setValue(LoginSharedPrefsState.NO_LOGIN_DATA)
        }
    }

}