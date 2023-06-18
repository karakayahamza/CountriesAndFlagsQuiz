package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.data.model.TriviaModel
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php")
    fun getTriviaData(
        @Query("amount") amount: Int?,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty : String?,
        @Query("type") type : String?
    ): io.reactivex.Observable<TriviaModel>
}