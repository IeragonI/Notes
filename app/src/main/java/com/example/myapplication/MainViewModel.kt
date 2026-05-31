package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedTag = MutableStateFlow<String?>(null)
    val selectedTag = _selectedTag.asStateFlow()

    val notes: StateFlow<List<Note>> = combine(
        searchQuery,
        selectedTag,
        repository.getAllNotes()
    ) { query, tag, allNotes ->
        allNotes.filter { note ->
            (query.isEmpty() || note.title.contains(query, ignoreCase = true)) &&
            (tag == null || note.tags.contains(tag))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags: StateFlow<List<String>> = repository.getAllNotes()
        .map { notes -> notes.flatMap { it.tags }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTagSelect(tag: String?) {
        _selectedTag.value = tag
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
