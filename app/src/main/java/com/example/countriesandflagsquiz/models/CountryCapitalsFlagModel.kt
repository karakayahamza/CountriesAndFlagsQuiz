package com.example.countriesandflagsquiz.models

import com.google.gson.annotations.SerializedName


class CountryCapitalsFlagModel(error: Boolean,msg :String,data:ArrayList<Data>) {

    @SerializedName("error")
    var error = false
    @SerializedName("msg")
    var msg: String?
    @SerializedName("data")
    var data: ArrayList<CountryCapitalsFlagModel.Data>

    class Data {
        var name: String? = null
        var capital: String? = null
        var iso2: String? = null
        var iso3: String? = null
    }

    init {
        this.error = error
        this.msg = msg
        this.data = data
    }
}