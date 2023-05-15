package com.example.todoplanner.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplanner.R

class RecycleViewAdapter(
    private val tasksData : ArrayList<TasksDataClass>,
    private val priorityData : ArrayList<PriorityDataClass>,
    private val statusData : ArrayList<StatusDataClass>,
    private val context : Context
) : RecyclerView.Adapter<RecycleViewAdapter.VH>() {

    private lateinit var mListener : OnRecycleViewListener

    interface OnRecycleViewListener {
        fun onRecycleViewClick (position : Int)
    }

    fun setOnRecycleViewClick(listener : OnRecycleViewListener) {
        mListener = listener
    }

    class VH(itemView : View, listener : OnRecycleViewListener)
        : RecyclerView.ViewHolder(itemView) {
        val status : TextView = itemView.findViewById(R.id.status)
        val header : TextView = itemView.findViewById(R.id.header)
        val priority : TextView = itemView.findViewById(R.id.priority)
        val responsible : TextView = itemView.findViewById(R.id.responsible)
        val deadline : TextView = itemView.findViewById(R.id.deadline)

        init {
            itemView.setOnClickListener {
                try{
                    listener.onRecycleViewClick(adapterPosition)
                } catch (ex : Exception){
                    Log.e("RecycleViewAdapter.kt", "Ошибка адаптера: "
                            + ex.stackTraceToString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : VH {
        return VH(
            LayoutInflater
                .from(context)
                .inflate(
                    R.layout.task_item,
                    parent,
                    false
                ),
            mListener
        )
    }

    override fun onBindViewHolder(holder : VH, position : Int) {
        try{
            val task = tasksData[holder.adapterPosition]
            val status = statusData[task.status_id.toInt() - 1].name
            val priority = priorityData[task.priority_id.toInt() - 1].name

            holder.header.text = task.header
            holder.header.setTextColor(CheckTask(context).getColor(task, statusData))
            holder.priority.text = context.getString(R.string.priorityWE, priority)
            holder.deadline.text = context.getString(R.string.deadlineWE, task.deadline)
            holder.responsible.text = context.getString(R.string.responsibleWE, task.responsible)
            holder.status.text = context.getString(R.string.statusWE, status)
        }
        catch (ex : Exception){
            Log.e("RecycleViewAdapter.kt", "Ошибка адаптера: " + ex.stackTraceToString())
        }
    }

    override fun getItemCount() : Int {
        return tasksData.size
    }
}