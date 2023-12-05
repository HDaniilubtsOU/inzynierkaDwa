package com.example.inzynierka

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel()
{
    var name = MutableLiveData<String>()
    var surname = MutableLiveData<String>()
    var mail = MutableLiveData<String>()
    var number = MutableLiveData<String>()
}