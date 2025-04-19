package com.example.todoapplicaton.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.todoapplicaton.data.ListEntries
import com.example.todoapplicaton.viewmodels.HomeScreenViewModel
import com.example.todoapplicaton.viewmodels.HomeScreenViewModelFactory

@Composable
fun HomeScreen(){
    val viewModel: HomeScreenViewModel = HomeScreenViewModelFactory(LocalContext.current).create(HomeScreenViewModel::class.java)
    val state = viewModel.tasks.collectAsState(initial = emptyList())
    val showDialog = remember { mutableStateOf(false) }

    // Handle the Add Task Dialog visibility
    AddTaskDialog(
        showDialog = showDialog.value,
        onDismiss = { showDialog.value = false },
        onAddTask = { newTask: ListEntries ->
            viewModel.addTask(newTask)  // Add the task to the database
            showDialog.value = false  // Close the dialog
        }
    )


    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.LightGray)
        ) {

            LazyColumn {
                items(state.value) { item ->
                    ListItem(item, onDelete = {viewModel.deleteTask(it)})
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(showDialog: Boolean, onDismiss: () -> Unit, onAddTask: (ListEntries) ->Unit) {
    if (showDialog) {
        val taskName = remember { mutableStateOf("") }
        val taskDate = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add New Task") },
            text = {
                Column {
                    TextField(
                        value = taskName.value,
                        onValueChange = { taskName.value = it },
                        label = { Text("Task Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = taskDate.value,
                        onValueChange = { taskDate.value = it },
                        label = { Text("Task Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskName.value.isNotEmpty() && taskDate.value.isNotEmpty()) {
                            onAddTask(ListEntries(task = taskName.value, date = taskDate.value))
                            onDismiss()
                        }
                    }
                ) {
                    Text("Add Task")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color("#2C7DA0".toColorInt())),
        title = {
            Text(text = "To Do List")
        }
    )
}

@Composable
fun ListItem(data: ListEntries, onDelete: (ListEntries) -> Unit){
    Row (
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxWidth()
            .padding(start=10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = data.id.toString())
        Column(modifier = Modifier.padding(start=13.dp)) {
            Text(text = data.task)
            Text(text = data.date)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onDelete(data) },){
            Icon(Icons.Filled.Done, contentDescription = "deleteButton")
        }
    }
}

@Composable
@Preview
fun PreviewTest(){
    HomeScreen()
}