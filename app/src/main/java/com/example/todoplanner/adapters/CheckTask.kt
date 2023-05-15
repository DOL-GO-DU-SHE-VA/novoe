package com.example.todoplanner.adapters

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todoplanner.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckTask(private val context : Context) {

    private val pattern = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun getColor(task : TasksDataClass, statusList : ArrayList<StatusDataClass>) : Int {
        try{
            val deadlineDate = pattern.parse(task.deadline)

            val dateToday = pattern.parse(pattern.format(Date()))

            val status = statusList[task.status_id.toInt() - 1].name

            return if (status == "Выполнена"){
                ContextCompat.getColor(context, R.color.green)
            } else if (dateToday!! > deadlineDate || status == "Отменена"){
                ContextCompat.getColor(context, R.color.red)
            } else {
                ContextCompat.getColor(context, R.color.black)
            }
        } catch (ex : Exception) {
            Log.e("CheckTask.kt", "Ошибки при получении цвета: " +
                    ex.stackTraceToString())
        }
        return 0
    }

    fun parseDate(dateOfCreation : String, deadline : String) : Boolean {
        try{
            val doc = pattern.parse(dateOfCreation)
            val dl = pattern.parse(deadline)
            return doc!! < dl
        } catch(ex : Exception) {
            Log.e("CheckTask.kt", "Ошибки при сравнивании дат: " +
                    ex.stackTraceToString())
        }
        return false
    }

    fun fixDate(day : Int, month : Int, year : Int) : String {
        return if(month + 1 < 10){
            "$day.0${month + 1}.$year"
        }
        else{
            "$day.${month + 1}.$year"
        }
    }
}