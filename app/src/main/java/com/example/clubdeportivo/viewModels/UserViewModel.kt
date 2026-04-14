package com.example.clubdeportivo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clubdeportivo.models.Usuario

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<Usuario?>()
    val user: LiveData<Usuario?> = _user

    fun setUser(usuario: Usuario) {
        _user.value = usuario
    }

    fun clearUser() {
        _user.value = null
    }
}