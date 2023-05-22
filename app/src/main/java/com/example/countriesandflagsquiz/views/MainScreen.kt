package com.example.countriesandflagsquiz.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.databinding.FragmentMainScreenBinding
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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

        /*binding.darkMode.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                context?.theme?.applyStyle(R.style.CountryTheme_NoActionBar_DarkMode, true)
            } else {
                context?.theme?.applyStyle(R.style.CountryTheme_NoActionBar, true)
            }
        }*/

        println(CapitalCity.toString())

        println(GuessFlag.toString())
        return view
    }
}