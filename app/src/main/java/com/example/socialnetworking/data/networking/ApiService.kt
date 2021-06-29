package com.example.socialnetworking.data.networking

import com.example.socialnetworking.utils.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()

    val remoteDataSource: ApiClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)

    private suspend fun <T> call(func: suspend () -> Response<T>): Response<T> {
        try {
            return func()
        } catch (exception: Exception) {
            throw CommunicationException()
        }
    }

    suspend fun <T> fetch(func: suspend () -> Response<T>): T {
        val response = call(func)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else if (response.code() == 401) {
            throw UnAuthenticatedException()
        } else if (response.code() == 422) {
            throw InvalidRequestException()
        } else {
            throw UnKnownException()
        }
    }
}