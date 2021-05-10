package com.ck.doordashproject.base.di

import android.content.Context
import androidx.room.Room
import com.ck.doordashproject.base.repository.network.APIConstants
import com.ck.doordashproject.base.repository.network.RxErrorHandlingCallAdapterFactory
import com.ck.doordashproject.base.repository.database.LikedDatabase
import com.ck.doordashproject.base.repository.network.DoorDashAPIs
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context) = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Singleton
    @Provides
    fun provideService() = Retrofit.Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            ).build()
        )
        .baseUrl(APIConstants.SERVER_API_ENDPOINT + "/")
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(DoorDashAPIs::class.java)

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        LikedDatabase::class.java, "liked.db"
    ).build()

    @Provides
    fun provideLikedDataDao(db: LikedDatabase) = db.getLikedDataDao()

    @Singleton
    @Provides
    fun providePicasso() = Picasso.get()
}
