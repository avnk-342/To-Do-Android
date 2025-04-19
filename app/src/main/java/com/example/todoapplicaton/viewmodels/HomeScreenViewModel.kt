package com.example.todoapplicaton.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapplicaton.data.ListDataDao
import com.example.todoapplicaton.data.ListDatabase
import com.example.todoapplicaton.data.ListEntries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val dao: ListDataDao): ViewModel(){
    private val _tasks = MutableStateFlow<List<ListEntries>>(emptyList())
    val tasks: StateFlow<List<ListEntries>> = _tasks

    init {
        viewModelScope.launch {
            dao.getAllTasks().collect {
                _tasks.value = it
            }
        }
    }

    fun addTask(entry: ListEntries){
        viewModelScope.launch {
            dao.upsertTask(entry)
            dao.getAllTasks().collect {
                _tasks.value = it
            }
        }
    }

    fun deleteTask(entry: ListEntries) {
        viewModelScope.launch {
            dao.deleteTask(entry)
            dao.getAllTasks().collect {
                _tasks.value = it
            }
        }
    }
}

class HomeScreenViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)){
            val dao = ListDatabase.getDatabase(context).listDataDao()
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}