package com.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
class ToDo(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "time") val time: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}