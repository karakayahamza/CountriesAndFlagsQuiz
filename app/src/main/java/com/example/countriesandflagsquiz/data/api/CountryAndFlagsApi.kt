package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.models.CountriesAndFlagsModel
import retrofit2.http.GET

interface CountryAndFlagsApi {
    @GET("countries/flag/images")
    fun getData(): io.reactivex.Observable<CountriesAndFlagsModel>
}