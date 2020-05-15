package com.memo.mynotes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.memo.mynotes.R
import com.memo.mynotes.databinding.NotesItemBinding
import com.memo.mynotes.room.entities.Note

class NotesAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getNoteAt(position: Int): Note {
        return differ.currentList[position]
    }


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binder: NotesItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notes_item,
            parent,
            false
        )

        return NoteViewHolder(binder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                holder.binder.interaction = interaction
                holder.binder.fav = R.drawable.ic_favorite
                holder.binder.noFav = R.drawable.ic_not_favorite
                holder.binder.note = differ.currentList[position]
            }
        }
    }

    class NoteViewHolder(val binder: NotesItemBinding) : RecyclerView.ViewHolder(binder.root)

    interface Interaction {
        fun onItemSelected(item: Note)
        fun onFavButtonClicked(note: Note)
    }
}

