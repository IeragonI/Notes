package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
