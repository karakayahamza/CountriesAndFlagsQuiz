package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.models.CountriesFlagsModel
import com.example.countriesandflagsquiz.models.CountryCapitalsFlagModel
import retrofit2.http.GET

interface CountryAndFlagsApi {
    @GET("countries/flag/images")
    fun getData(): io.reactivex.Observable<CountriesFlagsModel>

    @GET("countries/capital")
    fun getCapitalData(): io.reactivex.Observable<CountryCapitalsFlagModel>
}