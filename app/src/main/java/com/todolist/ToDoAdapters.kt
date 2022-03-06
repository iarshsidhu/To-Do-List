package com.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_items.view.*

class ToDoAdapters(
    private val context: Context,
    private val listClickDeleteInterface: ListClickDeleteInterface
) :
    RecyclerView.Adapter<ToDoAdapters.MyViewHolder>() {

    val list = ArrayList<ToDo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_items, parent, false)
        )
        return view
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentToDO = list[position]
        holder.textView.text = currentToDO.text
        holder.timeTextView.text = currentToDO.time
        holder.deleteButton.setOnClickListener {
            listClickDeleteInterface.onDeleteIconClick(list.get(position))
        }
        holder.itemView.apply {
            serialNumber.text = (position + 1).toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val textView: TextView = itemview.findViewById(R.id.listText)
        val deleteButton: ImageView = itemview.findViewById(R.id.deleteButton)
        val timeTextView: TextView = itemview.findViewById(R.id.listTimeText)
    }

    fun updateList(newList: List<ToDo>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

interface ListClickDeleteInterface {
    fun onDeleteIconClick(todo: ToDo)
}
