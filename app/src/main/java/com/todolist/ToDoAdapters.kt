package com.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_items.view.*

class ToDoAdapters(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<ToDoAdapters.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_items, parent, false)
        )
    }

    fun okButton(todo: ToDoData) {
        list.add(todo)
        notifyItemInserted(list.size - 1)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {
            listText.text = list[position].title
            serialNumber.text= (position+1).toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    }
}