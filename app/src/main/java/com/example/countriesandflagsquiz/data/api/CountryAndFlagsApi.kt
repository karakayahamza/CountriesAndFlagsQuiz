package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.models.CountriesFlagsModel
import retrofit2.http.GET

interface CountryAndFlagsApi {
    @GET("countries/flag/images")
    fun getData(): io.reactivex.Observable<CountriesFlagsModel>
}