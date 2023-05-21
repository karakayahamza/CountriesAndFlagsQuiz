package com.example.countriesandflagsquiz.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.databinding.FragmentChoseCountryOfCityBinding
import com.example.countriesandflagsquiz.models.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.randomFlags
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class ChoseCountryOfCity : Fragment() {
    private var _binding: FragmentChoseCountryOfCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    private var score = 0
    private var condition = false
    private lateinit var model : CountryCapitalsFlagModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoseCountryOfCityBinding.inflate(inflater, container, false)


        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]
        viewModel.loadCapitalData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog.show(requireContext(),
            "Wait",
            "Downloading...",
            true)

        viewModel.countriesCapital.observe(viewLifecycleOwner) { country ->
            country.let {
                model = it!!
            }
            progressDialog.dismiss()
            loadNewQuestion()
        }

    }
    @SuppressLint("ResourceAsColor")
    private fun setUpListener(capitalName :String){
        if (capitalName==""){
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
            if (checkAnswer(binding.aOption.text.toString(),capitalName)){
                binding.aOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.aOption.setBackgroundResource(R.drawable.wrong_answer)
        }
        binding.bOption.setOnClickListener {
            if (checkAnswer(binding.bOption.text.toString(),capitalName)){
                binding.bOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.bOption.setBackgroundResource(R.drawable.wrong_answer)
        }
        binding.cOption.setOnClickListener {
            if (checkAnswer(binding.cOption.text.toString(),capitalName)){
                binding.cOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.cOption.setBackgroundResource(R.drawable.wrong_answer)
        }
        binding.dOption.setOnClickListener {
            if (checkAnswer(binding.dOption.text.toString(),capitalName)){
                binding.dOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.dOption.setBackgroundResource(R.drawable.wrong_answer)
        }
    }

    @SuppressLint("SetTextI18n")
    fun checkAnswer(answer : String, capitalName:String):Boolean{

        if (answer==capitalName){
            score += 1
            condition = true
            binding.score.text = "Correct Answers: $score"

            binding.next.isClickable = true

            binding.next.setOnClickListener {
                binding.aOption.setBackgroundResource(R.drawable.buttun_design)
                binding.bOption.setBackgroundResource(R.drawable.buttun_design)
                binding.cOption.setBackgroundResource(R.drawable.buttun_design)
                binding.dOption.setBackgroundResource(R.drawable.buttun_design)
                loadNewQuestion()
                binding.next.isClickable = false
            }
        }
        else{
            condition = false
            binding.score.text = "Correct Answers: $score"

            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Correct Answer: $capitalName \n Score: $score \n Game Over")
            alert.setMessage("Try Again?")
            alert.setPositiveButton("Yes") { _, _ ->
                score = 0
                loadNewQuestion()
            }
            alert.setNegativeButton("No") { _, _ ->
                val action = ChoseCountryOfCityDirections.actionChoseCountryOfCityToMainScreen()
                Navigation.findNavController(binding.root).navigate(action)
                onDestroy()
            }
            alert.show()
        }
        return condition
    }

    fun loadNewQuestion(){
        var capitalName=""
        val countries = randomFlags()


        binding.aOption.text = model.data[countries.elementAtOrNull(0)!!].capital.toString()
        binding.bOption.text = model.data[countries.elementAtOrNull(1)!!].capital.toString()
        binding.cOption.text = model.data[countries.elementAtOrNull(2)!!].capital.toString()
        binding.dOption.text = model.data[countries.elementAtOrNull(3)!!].capital.toString()

        val choseRandomAnswer = model.data[countries.random()]


        binding.countryName.text = choseRandomAnswer.name
        capitalName= choseRandomAnswer.capital.toString()


        println(model.data[countries.elementAtOrNull(0)!!].capital.toString()+"/"+
                model.data[countries.elementAtOrNull(1)!!].capital.toString()+"/"+
                model.data[countries.elementAtOrNull(2)!!].capital.toString()+"/"+
                model.data[countries.elementAtOrNull(3)!!].capital.toString())

        println(choseRandomAnswer.name+"Country")
        println(capitalName+"Capital")


        setUpListener(capitalName)
    }
}