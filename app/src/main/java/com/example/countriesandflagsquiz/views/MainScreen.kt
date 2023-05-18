package com.example.countriesandflagsquiz.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.databinding.FragmentMainScreenBinding
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment



        binding.guessFlag.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToGuessFlag()
            Navigation.findNavController(it).navigate(action)
        }

        binding.findCapitalCity.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToChoseCountryOfCity()
            Navigation.findNavController(it).navigate(action)
        }
        binding.guessCityWhichCountry.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToWhichCountry()
            Navigation.findNavController(it).navigate(action)
        }
        binding.settings.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToSettings2()
            Navigation.findNavController(it).navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]

        viewModel.loadData()

        viewModel.countriesAndFlags.observe(viewLifecycleOwner) { counrty ->
            counrty.let {
            }
        }
    }
}