package com.example.countriesandflagsquiz.apis

import com.example.countriesandflagsquiz.models.CountriesAndFlagsModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

    class CountriesAndFlagsApiService {
    private val URL = "https://countriesnow.space/api/v0.1/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CountryAndFlagsApi::class.java)

    fun getData(): io.reactivex.Observable<CountriesAndFlagsModel> {
        return retrofit.getData()
    }
}