package com.example.countriesandflagsquiz.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.databinding.FragmentMainScreenBinding
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var quizViewModel: CountriesAndFlagsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.guessFlag.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToGuessFlag()
            Navigation.findNavController(it).navigate(action)
        }

        binding.findCapitalCity.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToCapitalCity()
            Navigation.findNavController(it).navigate(action)
        }

        binding.settings.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToSettings2()
            Navigation.findNavController(it).navigate(action)
        }



        val apiService = ApiServiceFactory.create()
        val quizRepository = CountryRepository(apiService)
        quizViewModel = CountriesAndFlagsViewModel(quizRepository)

        quizViewModel.loadData()

        quizViewModel.countriesAndFlags.observe(viewLifecycleOwner, Observer {

            println(it?.data!![0].name)
            println(it.data[45].name)
            println(it.data[70].name)
            println(it.data[88].name)


            println("Hello")
        })


        return view
    }
}