package com.example.todoplanner.di

import com.example.todoplanner.adapters.*
import retrofit2.http.*

interface ApiClient {
    @POST("/api/auth/signIn")
    suspend fun getToken(@Body dataClass : ApiPostToken) : ApiGetToken

    @GET("api/data/status")
    suspend fun getAllStatus() : ArrayList<StatusDataClass>

    @GET("api/data/priority")
    suspend fun getAllPriority() : ArrayList<PriorityDataClass>

    @POST("api/data/auth/")
    suspend fun regAuth(@Body dataClass : AuthDataClass) : AuthDataClass

    @GET("api/data/auth/{login}/{password}")
    suspend fun login(
        @Path("login") login : String,
        @Path("password") password : String
    ) : AuthDataClass

    @POST("api/data/username/{id}/role/{role}")
    suspend fun updateRole(
        @Path("id") id : Long,
        @Path("role") role : Long
    )

    @GET("api/data/auth/username/{id}")
    suspend fun getUsernameFromAuth(@Path("id") id : Long) : UserNameDataClass

    @GET("api/data/username")
    suspend fun getAllUsername() : ArrayList<UserNameDataClass>

    @GET("api/data/username/{id}")
    suspend fun getUsername(@Path("id")id : Long) : UserNameDataClass

    @POST("api/data/username/")
    suspend fun regUsername(@Body dataClass : UserNameDataClass) : UserNameDataClass

    @GET("api/data/tasks")
    suspend fun getAllTasks() : ArrayList<TasksDataClass>

    @GET("api/data/tasks/{id}")
    suspend fun getTask(@Path("id") id : Long) : TasksDataClass

    @POST("api/data/tasks/")
    suspend fun createTask(@Body dataClass : TasksDataClass) : TasksDataClass

    @DELETE("api/data/tasks/{id}")
    suspend fun deleteTask(@Path("id") id : Long)

    @POST("api/data/tasks/{id}/{status_id}/{date}")
    suspend fun updateStatus(
        @Path("id") id : Long,
        @Path("status_id") status_id : Long,
        @Path("date") date : String
    )

    @POST("api/data/tasks/{created_by}/{date_of_creation}/{date_of_updater}/{description}/{deadline}/{id_for_who}/{header}/{priority_id}/{responsible}/{status_id}/{owner_id}/{id}")
    suspend fun updateTask(
        @Path("created_by") created_by: Long,
        @Path("date_of_creation") date_of_creation: String,
        @Path("date_of_updater") date_of_updater: String,
        @Path("description") description: String,
        @Path("deadline") deadline: String,
        @Path("id_for_who") id_for_who: Long,
        @Path("header") header: String,
        @Path("priority_id") priority_id: Long,
        @Path("responsible") responsible: String,
        @Path("status_id") status_id: Long,
        @Path("owner_id") owner_id: Long,
        @Path("id") id : Long
    )
}