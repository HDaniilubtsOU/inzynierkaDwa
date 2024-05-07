package com.example.inzynierka

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inzynierka.data.UserDAO
import com.example.inzynierka.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel: ViewModel()
{
    var name = MutableLiveData<String>()
    var surname = MutableLiveData<String>()
    var mail = MutableLiveData<String>()
    var number = MutableLiveData<String>()

    fun setUser(user: UserEntity) {
        name.value = user.name
        surname.value = user.surname
        mail.value = user.email
        number.value = user.number.toString()
    }
}