package com.memo.mynotes.repositories

import androidx.lifecycle.LiveData
import com.memo.mynotes.room.dao.NoteDao
import com.memo.mynotes.room.entities.Note

class NotesRepo(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteAll() {
        noteDao.deleteAllNotes()
    }


}