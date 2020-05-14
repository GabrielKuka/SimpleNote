package com.memo.mynotes.ui.activities

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

        add_note_button.setOnClickListener {
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
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("EXTRA_TITLE")
            val content = data?.getStringExtra("EXTRA_CONTENT")
            val favorite = data?.getBooleanExtra("EXTRA_FAVORITE", false)
            val date = data?.getStringExtra("CREATION_DATE")


            val note = Note(0, title!!, content!!, favorite!!, date!!)
            homePageVM.insert(note)
            MessageHandler.displayToast(this, "Note added.")

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            val id = data?.getIntExtra("EXTRA_ID", -1)

            if (id == -1) {
                MessageHandler.displayToast(this, "Something went wrong.")
                return
            }

            val title = data?.getStringExtra("EXTRA_TITLE")
            val content = data?.getStringExtra("EXTRA_CONTENT")
            val favorite = data?.getBooleanExtra("EXTRA_FAVORITE", false)
            val date = data?.getStringExtra("CREATION_DATE")

            val note = Note(id!!, title!!, content!!, favorite!!, date!!)
            homePageVM.update(note)

        } else {
            MessageHandler.displayToast(this, "Note discarded.")
        }

    }

    private fun initRecyclerView() {
        notes_rv.apply {

            notesAdapter = NotesAdapter(this@HomePage)
            adapter = notesAdapter
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(notes_rv)
        FabHandler(notes_rv, add_note_button).start()
    }

    override fun onItemSelected(item: Note) {
        val intent = Intent(this, AddEditNote::class.java)
        intent.putExtra("EXTRA_ID", item.id)
        intent.putExtra("EXTRA_TITLE", item.title)
        intent.putExtra("EXTRA_CONTENT", item.content)
        intent.putExtra("EXTRA_FAVORITE", item.isFavorite)

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
