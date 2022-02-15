package com.todolist

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var todoadapters: ToDoAdapters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoadapters = ToDoAdapters(mutableListOf())
        recycler.adapter = todoadapters
        recycler.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            customDialog()
        }
    }

    fun customDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.custom_dialog, null)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.show()

        dialogView.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.okButton.setOnClickListener {
            val todo = dialogView.userText.text.toString()
            val user = ToDoData(todo)
            todoadapters.okButton(user)
            alertDialog.dismiss()
        }

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit")
            .setIcon(R.drawable.ic_baseline_error_24)

        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            finish()
        })
        val dialog = builder.create()
        dialog.show()
    }
}