package com.example.todoapplicaton.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todolist")
data class ListEntries(

    val task: String,
    val date: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int=0
)