package com.example.countriesandflagsquiz.data.repository

import com.example.countriesandflagsquiz.data.api.CountryAndFlagsApi
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel

class CountryRepository(private val apiService: CountryAndFlagsApi) {
    fun getFlagData(): io.reactivex.Observable<CountriesFlagsModel> {
        return apiService.getFlagData()
    }

    fun getCapitalData(): io.reactivex.Observable<CountryCapitalsFlagModel>{
        return apiService.getCapitalData()
    }
}