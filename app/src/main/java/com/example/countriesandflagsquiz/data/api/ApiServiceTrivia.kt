package com.example.countriesandflagsquiz.data.api

import com.example.countriesandflagsquiz.data.model.TriviaModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTrivia {

    private  val BASE_URL = "https://opentdb.com/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TriviaApi::class.java)

    fun getTrivia(amount:Int,category:Int, difficulty:String,type:String): io.reactivex.Observable<TriviaModel>{
         return retrofit.getTriviaData(amount,category,difficulty,type)
    }

}