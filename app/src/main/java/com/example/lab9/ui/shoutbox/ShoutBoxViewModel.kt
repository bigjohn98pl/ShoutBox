package com.example.lab9.ui.shoutbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoutBoxViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is shoutbox Fragment"
    }
    val text: LiveData<String> = _text
}