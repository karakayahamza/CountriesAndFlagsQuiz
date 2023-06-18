package com.example.countriesandflagsquiz.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TriviaModel(response_code: Int,results: ArrayList<Result>?) : Serializable {

    @SerializedName("response_code")
    var response_code = 0
    @SerializedName("results")
    var results: ArrayList<Result>? = null


    class Result {
        @SerializedName("category")
        var category: String? = null
        @SerializedName("type")
        var type: String? = null
        @SerializedName("difficulty")
        var difficulty: String? = null
        @SerializedName("question")
        var question: String? = null
        @SerializedName("correct_answer")
        var correct_answer: String? = null
        @SerializedName("incorrect_answers")
        var incorrect_answers: ArrayList<String>? = null
    }

    init {
       this.response_code = response_code
       this.results = results
    }

}