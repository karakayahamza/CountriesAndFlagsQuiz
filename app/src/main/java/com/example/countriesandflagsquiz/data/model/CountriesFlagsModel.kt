package com.example.countriesandflagsquiz.data.model

import com.google.gson.annotations.SerializedName

class CountriesFlagsModel(error: Boolean,msg :String,data:ArrayList<Data>) {

    @SerializedName("error")
    var error = false
    @SerializedName("msg")
    var msg: String?
    @SerializedName("data")
    var data: ArrayList<Data>

    class Data {
        @SerializedName("name")
        var name: String? = null
        @SerializedName("flag")
        var flag: String? = null
        @SerializedName("iso2")
        var iso2: String? = null
        @SerializedName("iso3")
        var iso3: String? = null
    }

    init {
        this.error = error
        this.msg = msg
        this.data = data
    }
}