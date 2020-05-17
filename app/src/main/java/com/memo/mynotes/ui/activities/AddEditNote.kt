package com.memo.mynotes.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.meet.quicktoast.Quicktoast
import com.memo.mynotes.R
import com.memo.mynotes.databinding.AddNoteActivityBinding
import com.memo.mynotes.room.entities.Note
import com.memo.mynotes.utils.AppData
import com.memo.mynotes.viewmodels.AddEditViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditNote : AppCompatActivity() {

    private lateinit var binder: AddNoteActivityBinding
    private lateinit var addEditViewModel: AddEditViewModel
    private var noteExtra: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder =
            DataBindingUtil.setContentView(this, R.layout.add_note_activity)

        noteExtra = intent.getParcelableExtra("note")

        binder.note = noteExtra

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        title = if (noteExtra != null) {
            "Edit Note"
        } else {
            "Add a note"
        }

        addEditViewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)
        addEditViewModel.initNote(noteExtra)

        initObservers()

        initColorButtons()

        binder.favImageView.setOnClickListener {
            addEditViewModel.setFavorite()
        }

    }

    private fun initObservers() {

        addEditViewModel.isFavorite().observe(this, androidx.lifecycle.Observer {
            if (it) {
                binder.favImageView.setImageResource(R.drawable.ic_favorite)
            } else {
                binder.favImageView.setImageResource(R.drawable.ic_not_favorite)
            }
        })

        addEditViewModel.getNoteColor().observe(this, androidx.lifecycle.Observer {
            binder.root.setBackgroundColor(it)
            if (it == AppData.BLUE || it == AppData.GREEN) {
                binder.titleEditText.setTextColor(AppData.WHITE)
                binder.contentEditText.setTextColor(AppData.WHITE)
            } else {
                binder.titleEditText.setTextColor(AppData.BLACK)
                binder.contentEditText.setTextColor(AppData.BLACK)
            }
        })

    }

    private fun initColorButtons() {

        binder.whiteButton.setOnClickListener { addEditViewModel.setColor(AppData.WHITE) }

        binder.blueButton.setOnClickListener { addEditViewModel.setColor(AppData.BLUE) }

        binder.greenButton.setOnClickListener { addEditViewModel.setColor(AppData.GREEN) }

        binder.pinkButton.setOnClickListener { addEditViewModel.setColor(AppData.PINK) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)

        if (noteExtra == null) {
            menu?.findItem(R.id.del_note)?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                // save note
                saveNote()
                true
            }
            R.id.del_note -> {
                // delete this note
                addEditViewModel.deleteNote(noteExtra!!)
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun saveNote() {
        val id = if (noteExtra == null) {
            -1
        } else {
            noteExtra?.id
        }
        val title = binder.titleEditText.text.toString().trim()
        val content = binder.contentEditText.text.toString().trim()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val favorite = addEditViewModel.isFavorite().value
        val noteColor = addEditViewModel.getNoteColor().value


        if (title.isEmpty() || content.isEmpty()) {
            Quicktoast(this).swarn("Fill all the fields.")
            return
        }

        val intent = Intent()

        val newNote =
            Note(id!!, title, content, favorite!!, creationDate = date, noteColor = noteColor!!)

        intent.putExtra("note", newNote)

        setResult(RESULT_OK, intent)
        finish()
    }


}
