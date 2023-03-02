package com.example.countriesandflagsquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.countriesandflagsquiz.models.CountriesAndFlagsModel
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CountriesAndFlagsViewModel
    private lateinit var weatherModel : CountriesAndFlagsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]

        viewModel.loadData()

        viewModel.countriesAndFlags.observe(this,{counrty ->
            counrty.let {

                println("Merhaba")
                println(it!!.data[0].name)

            }
        })


    }
}