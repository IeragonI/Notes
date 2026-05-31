package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    // Draft methods
    @Query("SELECT * FROM drafts WHERE id = 1")
    suspend fun getDraft(): Draft?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: Draft)

    @Query("DELETE FROM drafts WHERE id = 1")
    suspend fun clearDraft()
}
