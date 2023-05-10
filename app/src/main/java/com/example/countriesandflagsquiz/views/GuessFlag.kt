package com.example.countriesandflagsquiz.views

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.randomFlags
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


class GuessFlag : Fragment() {
    private var _binding: FragmentGuessFlagBinding? = null
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
        _binding = FragmentGuessFlagBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]

        viewModel.loadData()

        var coutryName= ""

        viewModel.countriesAndFlags.observe(viewLifecycleOwner) { counrty ->
            counrty.let {
                val countries = randomFlags()

                binding.aOption.text = counrty!!.data[countries.elementAtOrNull(0)!!].name.toString()
                binding.bOption.text = counrty.data[countries.elementAtOrNull(1)!!].name.toString()
                binding.cOption.text = counrty.data[countries.elementAtOrNull(2)!!].name.toString()
                binding.dOption.text = counrty.data[countries.elementAtOrNull(3)!!].name.toString()

               val choseRandomAnswer = counrty.data[countries.random()]
               GlideToVectorYou.justLoadImage(requireActivity(), Uri.parse(choseRandomAnswer.flag.toString()), binding.flagImage)
               coutryName= choseRandomAnswer.name.toString()
            }
            setUpListener(coutryName)
        }

    }
    fun setUpListener(coutryName :String){
        if (coutryName==""){
            binding.aOption.isClickable = false
            binding.bOption.isClickable = false
            binding.cOption.isClickable = false
            binding.dOption.isClickable = false
        }
        else{
            binding.aOption.isClickable = true
            binding.bOption.isClickable = true
            binding.cOption.isClickable = true
            binding.dOption.isClickable = true
        }
        binding.aOption.setOnClickListener {
            checkAnswer(binding.aOption.text.toString(),coutryName)
        }
        binding.bOption.setOnClickListener {
            checkAnswer(binding.bOption.text.toString(),coutryName)
        }
        binding.cOption.setOnClickListener {
            checkAnswer(binding.cOption.text.toString(),coutryName)
        }
        binding.dOption.setOnClickListener {
            checkAnswer(binding.dOption.text.toString(),coutryName)
        }
    }

    fun checkAnswer(answer : String,coutryName:String){
        if (answer==coutryName){
            Toast.makeText(requireContext(),"Answer Correct",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireContext(),"Answer Wrong",Toast.LENGTH_SHORT).show()
        }
    }
}