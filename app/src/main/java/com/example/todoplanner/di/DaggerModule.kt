package com.example.todoplanner.di

import android.content.Context
import androidx.room.Room
import com.example.todoplanner.repositories.AddTaskRepository
import com.example.todoplanner.repositories.LoginRepository
import com.example.todoplanner.repositories.SignUpRepository
import com.example.todoplanner.repositories.SplashRepository
import com.example.todoplanner.repositories.EditTaskRepository
import com.example.todoplanner.repositories.ListOfTasksRepository
import com.example.todoplanner.repositories.OneTaskRepository
import com.example.todoplanner.room.AppDatabase
import com.example.todoplanner.room.RoomRepository
import com.example.todoplanner.room.UsernameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class DaggerModule {
    // URL для подключения к API
    @Provides
    @Named("BASE_URL")
    fun provideBaseURL() : String = "http://10.111.207.156:8080/"
    // Получение токена
    @Provides
    @Singleton
    @Named("JWTRetrofit")
    fun provideJWTRetrofit(@Named("BASE_URL") BASE_URL : String) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("JWTService")
    fun provideJWTService(@Named("JWTRetrofit") retrofit : Retrofit) : ApiClient =
        retrofit.create(ApiClient::class.java)
    // Название файла
    @Provides
    @Named("SharedName")
    fun providesSharedName() : String = "token"

    @Provides
    @Named("GetToken")
    fun providesGetToken(@ApplicationContext context : Context, @Named("SharedName") name : String) : String {
        var token = ""
        while(token == ""){
            token = context
                .getSharedPreferences(name, Context.MODE_PRIVATE)
                .getString("token", "")
                .toString()
        }
        return token
    }
    // Инициализация бд
    @Provides
    @Singleton
    @Named("Room")
    fun provideRoom(@ApplicationContext context : Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "username"
        ).build()
    }
    // Инициализация интерфейса
    @Provides
    @Singleton
    @Named("roomService")
    fun provideServiceRoom(@Named("Room") ad : AppDatabase) : UsernameDao {
        return ad.usernameDao()
    }
    // Инициализация репозитория
    @Provides
    @Singleton
    @Named("roomRepository")
    fun provideRepositoryRoom(@Named("roomService") service : UsernameDao ) : RoomRepository =
        RoomRepository(service)
    // Авторизация в API с помощью токена
    @Provides
    @Named("Client")
    fun provideClient(@Named("GetToken") token : String) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest : Request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.build()
    }
    // Подключение к API
    @Provides
    @Singleton
    @Named("Retrofit")
    fun provideRetrofit(@Named("BASE_URL") BASE_URL : String, @Named("Client") client : OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    // Добавление интерфейса для взаимодействий с API
    @Provides
    @Singleton
    @Named("Service")
    fun provideService(@Named("Retrofit") retrofit : Retrofit) : ApiClient =
        retrofit.create(ApiClient::class.java)

    @Provides
    @Singleton
    @Named("SplashRepository")
    fun provideScreenRepository(@Named("JWTService") service : ApiClient) : SplashRepository =
        SplashRepository(service)

    @Provides
    @Singleton
    @Named("LoginRepository")
    fun provideLoginRepository(@Named("Service") service : ApiClient) : LoginRepository =
        LoginRepository(service)

    @Provides
    @Singleton
    @Named("SignUpRepository")
    fun provideRegRepository(@Named("Service") service : ApiClient) : SignUpRepository =
        SignUpRepository(service)

    @Provides
    @Singleton
    @Named("listRepository")
    fun provideListRepository(@Named("Service") service : ApiClient) : ListOfTasksRepository =
        ListOfTasksRepository(service)

    @Provides
    @Singleton
    @Named("addTaskRepository")
    fun provideAddTaskRepository(@Named("Service") service : ApiClient) : AddTaskRepository =
        AddTaskRepository(service)

    @Provides
    @Singleton
    @Named("oneTaskRepository")
    fun provideOneTaskRepository(@Named("Service") service: ApiClient) : OneTaskRepository =
        OneTaskRepository(service)

    @Provides
    @Singleton
    @Named("editTaskRepository")
    fun provideEditTaskRepository(@Named("Service") service: ApiClient) : EditTaskRepository =
        EditTaskRepository(service)
}