package com.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ToDoRepository
    val allToDo: LiveData<List<ToDo>>

    init {
        val dao = ToDoDatabase.getDatabase(application).getToDoDao()
        repository = ToDoRepository(dao)
        allToDo = repository.allToDo
    }

    fun deleteToDo(toDo: ToDo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(toDo)
    }

    fun insertToDO(toDo: ToDo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(toDo)
    }

}