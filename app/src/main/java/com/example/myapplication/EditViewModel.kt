package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _body = MutableStateFlow("")
    val body = _body.asStateFlow()

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags = _tags.asStateFlow()

    private var editingNoteId: Int? = null

    fun loadNote(id: Int?) {
        // Reset state
        editingNoteId = if (id != null && id != -1) id else null
        _title.value = ""
        _body.value = ""
        _tags.value = emptyList()

        viewModelScope.launch {
            if (id != null && id != -1) {
                val note = repository.getNoteById(id)
                if (note != null) {
                    _title.value = note.title
                    _body.value = note.body
                    _tags.value = note.tags
                }
            } else {
                val draft = repository.getDraft()
                if (draft != null) {
                    _title.value = draft.title
                    _body.value = draft.body
                    _tags.value = draft.tags
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onBodyChange(value: String) {
        _body.value = value
    }

    fun addTag(tag: String) {
        if (tag.isNotBlank() && !_tags.value.contains(tag)) {
            _tags.value = _tags.value + tag
        }
    }

    fun removeTag(tag: String) {
        _tags.value = _tags.value - tag
    }

    fun saveNote(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val note = Note(
                id = editingNoteId ?: 0,
                title = title.value,
                body = body.value,
                tags = tags.value
            )
            if (editingNoteId == null) {
                repository.insertNote(note)
                repository.clearDraft()
            } else {
                repository.updateNote(note)
            }
            onSuccess()
        }
    }

    fun saveAsDraft() {
        // Only save draft for new notes that have some content
        if (editingNoteId == null && (title.value.isNotBlank() || body.value.isNotBlank())) {
            viewModelScope.launch {
                repository.saveDraft(Draft(title = title.value, body = body.value, tags = tags.value))
            }
        } else if (editingNoteId == null) {
             // Clear draft if content was deleted
            viewModelScope.launch {
                repository.clearDraft()
            }
        }
    }
}
