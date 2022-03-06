package com.todolist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

    @Query("Select * from todo_table order by id ASC")
    fun getAllToDo(): LiveData<List<ToDo>>
}