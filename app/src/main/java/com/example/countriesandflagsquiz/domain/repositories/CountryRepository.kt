package com.example.countriesandflagsquiz.domain.repositories

import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import retrofit2.http.GET

interface CountryRepository {
    @GET("countries/flag/images")
    fun getData(): io.reactivex.Observable<CountriesFlagsModel>

    @GET("countries/capital")
    fun getCapitalData(): io.reactivex.Observable<CountryCapitalsFlagModel>
}