package com.example.countriesandflagsquiz.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.databinding.FragmentCapitalCityBinding
import com.example.countriesandflagsquiz.models.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel

class CapitalCity : Fragment() {
    private var _binding: FragmentCapitalCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    private var score = 0
    private var condition = false
    private lateinit var model : CountryCapitalsFlagModel
    private var max_score : Int = 0

    companion object {
        private const val SHARED_PREFS_FILE_NAME = "CAPITAL_MAX_SCORE"
        private const val CAPITAL_MAX_SCORE_KEY_NAME = "name"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        max_score = getNameFromSharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCapitalCityBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]
        viewModel.loadCapitalData()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
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
        binding.score.text = "Max Score: $max_score Correct Answers: $score"

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
            binding.score.text = "Max Score: $max_score Correct Answers: $score"

            binding.next.isClickable = true

            binding.aOption.isClickable = false
            binding.bOption.isClickable = false
            binding.cOption.isClickable = false
            binding.dOption.isClickable = false

            binding.next.setOnClickListener {
                binding.aOption.setBackgroundResource(R.drawable.buttun_design)
                binding.bOption.setBackgroundResource(R.drawable.buttun_design)
                binding.cOption.setBackgroundResource(R.drawable.buttun_design)
                binding.dOption.setBackgroundResource(R.drawable.buttun_design)

                binding.aOption.isClickable = true
                binding.bOption.isClickable = true
                binding.cOption.isClickable = true
                binding.dOption.isClickable = true
                loadNewQuestion()
                binding.next.isClickable = false
            }
        }
        else{
            condition = false
            binding.score.text = "Max score: $max_score Correct Answers: $score"

            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Correct Answer: $capitalName \n Score: $score \n Game Over")
            alert.setMessage("Try Again?")
            alert.setPositiveButton("Yes") { _, _ ->
                score = 0
                loadNewQuestion()
            }
            alert.setNegativeButton("No") { _, _ ->
                val action = CapitalCityDirections.actionCapitalCityToMainScreen()
                Navigation.findNavController(binding.root).navigate(action)
                onDestroy()
            }
            alert.show()

            if (getNameFromSharedPreferences(requireContext())<score){
                saveNameToSharedPreferences(requireContext(),score)
            }
        }
        return condition
    }

    private fun loadNewQuestion(){
        val capitalName: String
        val countries = randomFlags()


        binding.aOption.text = model.data[countries.elementAtOrNull(0)!!].capital.toString()
        binding.bOption.text = model.data[countries.elementAtOrNull(1)!!].capital.toString()
        binding.cOption.text = model.data[countries.elementAtOrNull(2)!!].capital.toString()
        binding.dOption.text = model.data[countries.elementAtOrNull(3)!!].capital.toString()

        val random = countries.random()
        val remainingNumbers = ArrayList(countries).apply { remove(random) }

        val choseRandomAnswer = model.data[random]


        binding.joker.setOnClickListener {
            fiftyPercentJoker(countries.indexOf(remainingNumbers[0]))
            fiftyPercentJoker(countries.indexOf(remainingNumbers[1]))
        }
        binding.countryName.text = choseRandomAnswer.name
        capitalName= choseRandomAnswer.capital.toString()

        setUpListener(capitalName)
    }

    private fun fiftyPercentJoker(index :Int){
        when (index) {
            0 -> {
                // Code to be executed when index is 0
                binding.aOption.isClickable = false
                binding.aOption.setBackgroundResource(R.drawable.used_joker)
            }
            1 -> {
                // Code to be executed when index is 1
                binding.bOption.isClickable = false
                binding.bOption.setBackgroundResource(R.drawable.used_joker)
            }
            2 -> {
                // Code to be executed when index is 2
                binding.cOption.isClickable = false
                binding.cOption.setBackgroundResource(R.drawable.used_joker)
            }
            3 -> {
                // Code to be executed when index is 2
                binding.dOption.isClickable = false
                binding.dOption.setBackgroundResource(R.drawable.used_joker)
            }
            else -> {
                println("Index is not 0, 1, or 2")
            }
        }
    }

    private fun saveNameToSharedPreferences(context: Context, name: Int) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(CAPITAL_MAX_SCORE_KEY_NAME, name)
        editor.apply()
    }

    private fun getNameFromSharedPreferences(context: Context): Int {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return (sharedPreferences.getInt(CAPITAL_MAX_SCORE_KEY_NAME, 0) ?: "") as Int
    }

    private fun randomFlags(): MutableSet<Int> {
        val randomNumbers = mutableSetOf<Int>()
        while (randomNumbers.size < 4) {
            randomNumbers.add((1..220).random())
        }
        return randomNumbers
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}