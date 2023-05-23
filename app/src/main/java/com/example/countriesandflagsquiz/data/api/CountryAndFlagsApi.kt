package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import retrofit2.http.GET

interface CountryAndFlagsApi {
    @GET("countries/flag/images")
    fun getFlagData(): io.reactivex.Observable<CountriesFlagsModel>

    @GET("countries/capital")
    fun getCapitalData(): io.reactivex.Observable<CountryCapitalsFlagModel>
}