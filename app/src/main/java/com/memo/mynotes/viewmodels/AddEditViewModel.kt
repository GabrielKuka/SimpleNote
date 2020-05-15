package com.memo.mynotes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.memo.mynotes.repositories.NotesRepo
import com.memo.mynotes.room.NoteDb
import com.memo.mynotes.room.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditViewModel(application: Application) : AndroidViewModel(application) {
    private val _isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun initNote(noteExtra: Note?) {
        _isFavorite.value = !(noteExtra == null || !noteExtra.isFavorite)
    }

    fun isFavorite(): LiveData<Boolean> = _isFavorite

    fun setFavorite() {
        _isFavorite.value = !_isFavorite.value!!
    }

    fun deleteNote(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        NotesRepo(NoteDb.getDatabase(getApplication()).noteDao()).delete(note)
    }

}