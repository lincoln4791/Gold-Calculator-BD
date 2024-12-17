package com.lincoln4791.goldcalculatorbd.di

import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.network.apiServices.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Replace with your API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
