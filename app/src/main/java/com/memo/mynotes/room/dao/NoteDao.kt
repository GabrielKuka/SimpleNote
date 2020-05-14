package com.memo.mynotes.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.memo.mynotes.room.entities.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

}