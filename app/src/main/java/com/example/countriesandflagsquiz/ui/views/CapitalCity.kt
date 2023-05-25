package com.example.countriesandflagsquiz.ui.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.databinding.FragmentCapitalCityBinding
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.helpers.getHighScore
import com.example.countriesandflagsquiz.helpers.loadNewQuestion
import com.example.countriesandflagsquiz.helpers.saveHighScore
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel

class CapitalCity : Fragment() {
    private var _binding: FragmentCapitalCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryViewModel: CountriesAndFlagsViewModel
    private var score = 0
    private lateinit var model : CountryCapitalsFlagModel
    private var maxScore : Int = 0

    companion object {
        private const val SHARED_PREFS_FILE_NAME = "CAPITAL_MAX_SCORE"
        private const val KEY_NAME = "name_CAPİTAL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxScore = getHighScore(requireContext(),SHARED_PREFS_FILE_NAME,
            KEY_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCapitalCityBinding.inflate(inflater, container, false)

        val apiService = ApiServiceFactory.create()
        val quizRepository = CountryRepository(apiService)
        countryViewModel = CountriesAndFlagsViewModel(quizRepository)

        countryViewModel.loadCapitalData()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog.show(requireContext(),
            "Wait",
            "Downloading...",
            true)

        countryViewModel.countriesCapital.observe(viewLifecycleOwner) { country ->
            country.let {
                model = it!!
            }
            progressDialog.dismiss()
            setUpListener(loadNewQuestion(model,binding))
        }
        binding.score.text = "Max Score: $maxScore Correct Answers: $score"
    }
    @SuppressLint("ResourceAsColor")
    fun setUpListener(capitalName :String){
        if (capitalName==""){
            disableOptions()
        }
        else{
            enableOptions()
        }

        binding.aOption.setOnClickListener {
            if (handleAnswer(binding.aOption.text.toString(),capitalName)){
                binding.aOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.aOption.setBackgroundResource(R.drawable.wrong_answer)

            println(binding.aOption.text)
        }
        binding.bOption.setOnClickListener {
            if (handleAnswer(binding.bOption.text.toString(),capitalName)){
                binding.bOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.bOption.setBackgroundResource(R.drawable.wrong_answer)

            println(binding.bOption.text)
        }
        binding.cOption.setOnClickListener {
            if (handleAnswer(binding.cOption.text.toString(),capitalName)){
                binding.cOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.cOption.setBackgroundResource(R.drawable.wrong_answer)

            println(binding.cOption.text)
        }
        binding.dOption.setOnClickListener {
            if (handleAnswer(binding.dOption.text.toString(),capitalName)){
                binding.dOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.dOption.setBackgroundResource(R.drawable.wrong_answer)

            println(binding.dOption.text)
        }

        binding.next.setOnClickListener {
            resetOptions()
            setUpListener(loadNewQuestion(model,binding))
            binding.next.isClickable = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleAnswer(answer: String, capitalName: String): Boolean {
        val result : Boolean
        if (answer == capitalName) {
            binding.score.text = "Max Score: $maxScore Correct Answers: ${++score}"
            binding.next.isClickable = true
            disableOptions()
            result = true
        } else {
            showGameOverDialog(capitalName)
            result = false
        }
        return result
    }
    private fun disableOptions() {
        val buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)
        for (button in buttonArray) {
            button.isClickable = false
        }
    }
    private fun enableOptions() {
        val buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)
        for (button in buttonArray) {
            button.isClickable = true
        }
    }
    private fun showGameOverDialog(capitalName:String) {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Game Over")
            .setMessage("Correct Answer: ${capitalName}\nScore: $score\nTry Again?")
            .setPositiveButton("Yes") { _, _ ->
                score = 0
                setUpListener(loadNewQuestion(model,binding))
            }
            .setNegativeButton("No") { _, _ ->
                val action = CapitalCityDirections.actionCapitalCityToMainScreen()
                Navigation.findNavController(binding.root).navigate(action)
                onDestroy()
            }
            .show()

        if (getHighScore(requireContext(), SHARED_PREFS_FILE_NAME, KEY_NAME) < score) {
            saveHighScore(requireContext(),score, SHARED_PREFS_FILE_NAME, KEY_NAME)
        }
    }
    private fun resetOptions() {
        val buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)
        val buttonDesign = R.drawable.buttun_design

        for (button in buttonArray) {
            button.setBackgroundResource(buttonDesign)
        }
        enableOptions()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}