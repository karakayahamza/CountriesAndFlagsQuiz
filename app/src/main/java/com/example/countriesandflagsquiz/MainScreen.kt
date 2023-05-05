package com.example.countriesandflagsquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.example.countriesandflagsquiz.databinding.FragmentMainScreenBinding
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel
import java.util.concurrent.TimeUnit

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
    ): View? {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]

        viewModel.loadData()

        viewModel.countriesAndFlags.observe(viewLifecycleOwner) { counrty ->
            counrty.let {

                println("Merhaba")
                println(it!!.data[0].name)

            }
        }

        binding.guessFlag.setOnClickListener {
            println("Clicked button.")
        }

        return view
    }
}