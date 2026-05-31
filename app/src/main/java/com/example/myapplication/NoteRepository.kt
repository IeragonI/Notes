package com.example.myapplication

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    
    fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query)
    
    suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)
    
    suspend fun insertNote(note: Note) = noteDao.insert(note)
    
    suspend fun updateNote(note: Note) = noteDao.update(note)
    
    suspend fun deleteNote(note: Note) = noteDao.delete(note)
    
    suspend fun getDraft(): Draft? = noteDao.getDraft()
    
    suspend fun saveDraft(draft: Draft) = noteDao.saveDraft(draft)
    
    suspend fun clearDraft() = noteDao.clearDraft()
}
