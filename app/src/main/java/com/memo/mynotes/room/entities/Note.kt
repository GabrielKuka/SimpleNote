package com.memo.mynotes.room.entities

import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "creation_date") val creationDate: String,
    @ColumnInfo(name = "note_color") val noteColor: Int = Color.WHITE
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is Note) {
            return false
        }

        if (id != other.id) {
            return false
        }

        if (title != other.title) {
            return false
        }

        if (content != other.content) {
            return false
        }

        if (isFavorite != other.isFavorite) {
            return false
        }

        if (creationDate != other.creationDate) {
            return false
        }

        if (noteColor != other.noteColor) {
            return false
        }


        return true
    }

}