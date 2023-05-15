package com.example.todoplanner.adapters

data class ApiGetToken(
    val token : String,
    val type : String,
    val id : Long,
    val username : String,
    val email : String,
    val roles : List<String>
)
