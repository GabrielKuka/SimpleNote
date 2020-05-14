package com.memo.mynotes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.memo.mynotes.room.dao.NoteDao
import com.memo.mynotes.room.entities.Note
import com.memo.mynotes.utils.Converters

@Database(entities = [Note::class], version = 3, exportSchema = false)
abstract class NoteDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDb? = null

        fun getDatabase(context: Context): NoteDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDb::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}