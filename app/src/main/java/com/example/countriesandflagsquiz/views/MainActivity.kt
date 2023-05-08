package com.example.countriesandflagsquiz.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.countriesandflagsquiz.databinding.ActivityMainBinding
import com.example.countriesandflagsquiz.models.CountriesAndFlagsModel
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}