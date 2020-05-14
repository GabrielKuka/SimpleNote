package com.memo.mynotes.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "creation_date") val creationDate: String
) {

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


        return true
    }

}