package com.example.todoplanner.adapters

data class TasksDataClass(
    val id : Long,
    val created_by : Long,
    val date_of_creation : String,
    val date_of_updater : String,
    val description : String,
    val deadline : String,
    val id_for_who : Long,
    val header : String,
    val priority_id : Long,
    val responsible : String,
    val status_id : Long,
    val owner_id : Long
)