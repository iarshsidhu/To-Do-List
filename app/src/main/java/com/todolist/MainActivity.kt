package com.todolist

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import java.util.*

class MainActivity : AppCompatActivity(), ListClickDeleteInterface {

    lateinit var viewModel: ToDoViewModel
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        recycler.layoutManager = LinearLayoutManager(this)
        val adapters = ToDoAdapters(this, this)
        recycler.adapter = adapters

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ToDoViewModel::class.java)
        viewModel.allToDo.observe(this, Observer { list ->
            list?.let {
                adapters.updateList(it)
            }
        })

        addButton.setOnClickListener {
            customDialog()
        }

    }

    override fun onDeleteIconClick(todo: ToDo) {
        viewModel.deleteToDo(todo)
        Toast.makeText(this, "${todo.text} Deleted", Toast.LENGTH_LONG).show()
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    @SuppressLint("SetTextI18n")
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
            val time = dialogView.timeTextView.text.toString()
            if (todo.isNotEmpty()) {
                viewModel.insertToDO(ToDo(todo, time))
                Toast.makeText(this, "$todo Inserted", Toast.LENGTH_LONG).show()
                setAlarm()
                alertDialog.dismiss()
            }
        }

        dialogView.timePickerButton.setOnClickListener {
            picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Text")
                .build()

            picker.show(supportFragmentManager, "Android")

            picker.addOnPositiveButtonClickListener {

                if (picker.hour > 12) {
                    dialogView.timeTextView.text =
                        String.format("%02d", picker.hour - 12) + " : " + String.format(
                            "%02d", picker.minute
                        ) + "PM"
                } else {
                    dialogView.timeTextView.text =
                        String.format("%02d", picker.hour) + " : " + String.format(
                            "%02d", picker.minute
                        ) + "AM"
                }

                calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = picker.hour
                calendar[Calendar.MINUTE] = picker.minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
            }
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Android Reminder Channel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Android", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }


}