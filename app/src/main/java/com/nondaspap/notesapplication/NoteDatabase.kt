package com.nondaspap.notesapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDAO

    // Single object of database class

    companion object {
        @Volatile
         private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    NoteDatabase::class.java,
                                              "note_database")
                                    .addCallback(NoteDatabaseCallback(scope))
                                    .build()

                INSTANCE = instance

                instance
            }
        }
    }


    private class NoteDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let {
                //database.getNoteDao().insert(Note("t", "t"))
                database ->
                scope.launch {
                    val noteDao = database.getNoteDao()
                    noteDao.insert(Note("title", "description"))
                    noteDao.insert(Note("title1", "description1"))
                    noteDao.insert(Note("title2", "description2"))
                    noteDao.insert(Note("title3", "description3"))
                }
            }
        }


    }

}