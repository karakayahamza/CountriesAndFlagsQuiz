package com.example.countriesandflagsquiz.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.databinding.FragmentMainScreenBinding
import com.example.countriesandflagsquiz.views.MainScreenDirections

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
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
        return view
    }
}