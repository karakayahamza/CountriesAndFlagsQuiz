package com.example.countriesandflagsquiz.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.countriesandflagsquiz.databinding.FragmentWhichCountryBinding
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class WhichCountry : Fragment() {
    private var _binding: FragmentWhichCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWhichCountryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]

        viewModel.loadData()

        viewModel.countriesAndFlags.observe(viewLifecycleOwner) { counrty ->
            counrty.let {

                println("Merhaba")
                println(it!!.data[0].name)

            }
        }
    }
}