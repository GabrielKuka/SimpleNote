package com.memo.mynotes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.memo.mynotes.repositories.NotesRepo
import com.memo.mynotes.repositories.SharedPrefsRepo
import com.memo.mynotes.room.NoteDb
import com.memo.mynotes.room.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomePageVM(application: Application) : AndroidViewModel(application) {

    private val notesRepo: NotesRepo = NotesRepo(NoteDb.getDatabase(application).noteDao())

    private val sharedPrefsRepo: SharedPrefsRepo = SharedPrefsRepo((application))

    private val allNotes: LiveData<List<Note>> = notesRepo.allNotes

    private val layoutPreference: MutableLiveData<Int> = MutableLiveData<Int>().apply {
        postValue(sharedPrefsRepo.getLayoutPref())
    }

    fun setLayoutPreference(value: Int) {
        sharedPrefsRepo.setLayoutPref(value)
        layoutPreference.postValue(value)
    }

    fun getLayoutPreference(): LiveData<Int> {
        return layoutPreference
    }

    fun insert(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        notesRepo.insert(note)
    }

    fun delete(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        notesRepo.delete(note)
    }

    fun update(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        notesRepo.update(note)
    }

    fun deleteAllNotes() = CoroutineScope(Dispatchers.Default).launch {
        notesRepo.deleteAll()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return this.allNotes
    }

    fun <T> MutableLiveData<T>.notifyObserver(){
        this.value = this.value
    }

}