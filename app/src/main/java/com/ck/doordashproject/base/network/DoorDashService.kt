package com.ck.doordashproject.base.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DoorDashService private constructor(retrofit: Retrofit){
    val service = retrofit.create(DoorDashAPIs::class.java)

    object Holder {
        val httpBuilder = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        val gson = GsonBuilder().setLenient().create()
        val doorDashService = DoorDashService(Retrofit.Builder()
            .client(httpBuilder.build())
            .baseUrl(APIConstants.SERVER_API_ENDPOINT + "/")
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build())
    }

    companion object {
        val INSTANCE: DoorDashService by lazy { Holder.doorDashService }
    }
}