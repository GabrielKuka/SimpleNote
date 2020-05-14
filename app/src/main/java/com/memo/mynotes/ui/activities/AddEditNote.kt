package com.memo.mynotes.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.memo.mynotes.R
import com.memo.mynotes.databinding.AddNoteActivityBinding
import com.memo.mynotes.utils.MessageHandler
import java.text.SimpleDateFormat
import java.util.*

class AddEditNote : AppCompatActivity() {

    private lateinit var binding: AddNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.add_note_activity)

        binding.intents = intent
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        title = if (hasIdExtra()) {
            "Edit Note"

        } else {
            "Add a note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                // save note
                saveNote()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun hasIdExtra(): Boolean {
        return intent.hasExtra("EXTRA_ID")
    }

    private fun saveNote() {

        val title = binding.titleEditText.text.toString().trim()
        val content = binding.contentEditText.text.toString().trim()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val favorite = intent.getBooleanExtra("EXTRA_FAVORITE", false)
        // add a checkbox and retrieve the value for favorite

        if (title.isEmpty() || content.isEmpty()) {
            MessageHandler.displayToast(this, "Fill all the fields.")
            return
        }

        val intent = Intent()
        intent.putExtra("EXTRA_TITLE", title)
        intent.putExtra("EXTRA_CONTENT", content)
        intent.putExtra(
            "EXTRA_FAVORITE",
            favorite
        )        // set the favorite to the value of checkbox
        intent.putExtra("CREATION_DATE", date)

        val id = getIntent().getIntExtra("EXTRA_ID", -1)
        if (id != -1) {
            intent.putExtra("EXTRA_ID", id)
        }

        setResult(RESULT_OK, intent)
        finish()
    }
}
