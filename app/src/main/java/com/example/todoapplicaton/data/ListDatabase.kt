package com.example.todoapplicaton.data

import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.Room

@Database(
    entities = [ListEntries::class],
    version = 1
)
abstract class ListDatabase: RoomDatabase(){
    abstract fun listDataDao(): ListDataDao
    companion object{
        @JvmStatic
        fun getDatabase(context: Context): ListDatabase{
            return Room.databaseBuilder(
                context,
                ListDatabase::class.java,
                "TodoList"
            ).build()
        }
    }
}