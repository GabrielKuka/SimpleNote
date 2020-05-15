package com.memo.mynotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.memo.mynotes.R
import com.memo.mynotes.databinding.HomePageActivityBinding
import com.memo.mynotes.room.entities.Note
import com.memo.mynotes.ui.adapters.NotesAdapter
import com.memo.mynotes.ui.dialogs.BasicDialog
import com.memo.mynotes.utils.FabHandler
import com.memo.mynotes.utils.MessageHandler
import com.memo.mynotes.viewmodels.HomePageVM
import kotlinx.android.synthetic.main.home_page_activity.*

class HomePage : AppCompatActivity(), NotesAdapter.Interaction {

    private lateinit var homePageVM: HomePageVM
    private lateinit var notesAdapter: NotesAdapter
    private val ADD_NOTE_REQUEST = 1
    private val EDIT_NOTE_REQUEST = 2

    private lateinit var binder: HomePageActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.home_page_activity)

        initRecyclerView()

        initViewModel()

        binder.addNoteButton.setOnClickListener {
            startActivityForResult(Intent(this, AddEditNote::class.java), ADD_NOTE_REQUEST)
        }

    }

    private fun initViewModel() {
        homePageVM = ViewModelProviders.of(this).get(HomePageVM::class.java)

        homePageVM.getAllNotes().observe(this, Observer {
            notesAdapter.apply {
                submitList(it)
            }

        })

        homePageVM.getLayoutPreference().observe(this, Observer {
            if (it == 0) {
                binder.layout = LinearLayoutManager(this)
            } else {
                binder.layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage_menu, menu)

        if (homePageVM.getLayoutPreference().value == 0) {
            menu?.findItem(R.id.layout_button)?.icon = getDrawable(R.drawable.ic_view_grid)
        } else {
            menu?.findItem(R.id.layout_button)?.icon = getDrawable(R.drawable.ic_view_list)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.layout_button -> {
                // Change layout manager

                if (homePageVM.getLayoutPreference().value == 1) {
                    homePageVM.setLayoutPreference(0)
                    item.icon = getDrawable(R.drawable.ic_view_grid)
                } else {
                    homePageVM.setLayoutPreference(1)
                    item.icon = getDrawable(R.drawable.ic_view_list)
                }
                true
            }
            R.id.del_all_button -> {
                // delete all notes
                val content = "Delete all notes?"
                BasicDialog(content) {
                    homePageVM.deleteAllNotes()
                }.show(supportFragmentManager, "")
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        notesAdapter.notifyDataSetChanged()

        if (resultCode == Activity.RESULT_OK) {
            val recNote = data?.getParcelableExtra<Note>("note")

            when (requestCode) {

                ADD_NOTE_REQUEST -> {

                    val note =
                        Note(
                            0,
                            recNote?.title!!,
                            recNote.content,
                            recNote.isFavorite,
                            recNote.creationDate
                        )

                    homePageVM.insert(note)

                    MessageHandler.displayToast(this, "Note added.")

                }
                EDIT_NOTE_REQUEST -> {

                    val note = Note(
                        recNote?.id!!,
                        recNote.title,
                        recNote.content,
                        recNote.isFavorite,
                        recNote.creationDate
                    )
                    homePageVM.update(note)
                }
            }

        } else {
            MessageHandler.displayToast(this, "Note discarded.")
        }

    }

    private fun initRecyclerView() {
        notesAdapter = NotesAdapter(this@HomePage)
        binder.notesRv.adapter = notesAdapter

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binder.notesRv)
        FabHandler(binder.notesRv, add_note_button).start()
    }

    override fun onItemSelected(item: Note) {
        val intent = Intent(this, AddEditNote::class.java)
        intent.putExtra("note", item)

        startActivityForResult(intent, EDIT_NOTE_REQUEST)
    }

    override fun onFavButtonClicked(note: Note) {
        val fav = !note.isFavorite
        val newNote = Note(note.id, note.title, note.content, fav, note.creationDate)
        homePageVM.update(newNote)
    }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                homePageVM.delete(notesAdapter.getNoteAt(viewHolder.adapterPosition))
            }

        }
}
