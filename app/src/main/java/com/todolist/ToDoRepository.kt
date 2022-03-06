package com.todolist

import androidx.lifecycle.LiveData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val allToDo: LiveData<List<ToDo>> = toDoDao.getAllToDo()

    suspend fun insert(toDo: ToDo) {
        toDoDao.insert(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        toDoDao.delete(toDo)
    }

}