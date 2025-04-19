package com.example.todoapplicaton.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDataDao {

    @Upsert
    suspend fun upsertTask(listEntries: ListEntries)

    @Delete
    suspend fun deleteTask(listEntries: ListEntries)

    @Query("SELECT * FROM Todolist")
    fun getAllTasks(): Flow<List<ListEntries>>
}