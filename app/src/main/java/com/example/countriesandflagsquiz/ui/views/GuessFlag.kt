@file:Suppress("DEPRECATION")

package com.example.countriesandflagsquiz.ui.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.helpers.getHighScore
import com.example.countriesandflagsquiz.helpers.loadNewQuestion
import com.example.countriesandflagsquiz.helpers.saveHighScore
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel


@Suppress("DEPRECATION")
class GuessFlag : Fragment() {
    private var _binding: FragmentGuessFlagBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryViewModel: CountriesAndFlagsViewModel
    private lateinit var model : CountriesFlagsModel
    private lateinit var decorView: ViewGroup
    lateinit var countDownTimer : CountDownTimer
    private var maxScore = 0
    private var score = 0
    private lateinit var buttonArray : Array<Button>

    companion object {
        private const val SHARED_PREFS_FILE_NAME = "GUESS_FLAG_MAX_SCORE"
        private const val GUESS_FLAG_SCORE_KEY_NAME = "name"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuessFlagBinding.inflate(inflater, container, false)

        val apiService = ApiServiceFactory.create()
        val quizRepository = CountryRepository(apiService)

        countryViewModel = CountriesAndFlagsViewModel(quizRepository)

        countryViewModel.loadData()

        maxScore = getHighScore(requireContext(), SHARED_PREFS_FILE_NAME, GUESS_FLAG_SCORE_KEY_NAME)

        decorView = requireActivity().window.decorView as ViewGroup
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog.show(requireContext(),
            "Wait",
            "Downloading...",
            true)

        countryViewModel.countriesAndFlags.observe(viewLifecycleOwner) { country ->
            country.let {
                model = it!!
            }
            progressDialog.dismiss()
            startTimer()
            setUpListener(loadNewQuestion(model,binding,countDownTimer))
        }
        binding.score.text = "Max Score: $maxScore Correct Answers: $score"
        buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)

    }
    @SuppressLint("ResourceAsColor")
    private fun setUpListener(countryName :String){
        for (button in buttonArray) {
            button.setOnClickListener {
                if (handleAnswer(button.text.toString(),countryName)){
                    nextQuestion()
                }
                else showGameOverDialog(countryName)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleAnswer(answer: String, countryName: String): Boolean {
        return answer==countryName
    }

    @SuppressLint("SetTextI18n")
    private fun nextQuestion(){
        binding.score.text = "Max Score: $maxScore Correct Answers: ${++score}"
        com.example.countriesandflagsquiz.helpers.setAnswerCheckLight(requireContext(),decorView,Color.GREEN)
        setUpListener(loadNewQuestion(model,binding,countDownTimer))
    }

    private fun showGameOverDialog(capitalName:String) {
        countDownTimer.cancel()
        disableOptions()
        com.example.countriesandflagsquiz.helpers.setAnswerCheckLight(requireContext(),decorView,Color.RED)
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Game Over")
            .setMessage("Correct Answer: ${capitalName}\nScore: $score\nTry Again?")
            .setPositiveButton("Yes") { _, _ ->
                score = 0
                setUpListener(loadNewQuestion(model,binding,countDownTimer))
            }
            .setNegativeButton("No") { _, _ ->
                val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                Navigation.findNavController(binding.root).navigate(action)
                onDestroy()
            }
            .show()

        if (getHighScore(requireContext(), SHARED_PREFS_FILE_NAME, GUESS_FLAG_SCORE_KEY_NAME) < score) {
            saveHighScore(requireContext(),score, SHARED_PREFS_FILE_NAME, GUESS_FLAG_SCORE_KEY_NAME)
        }
    }

    fun startTimer(){
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle("Time is over")
                alert.setMessage("Try Again?")
                alert.setPositiveButton("Yes") { _, _ ->
                    startTimer()
                    setUpListener(loadNewQuestion(model,binding,countDownTimer))
                }
                alert.setNegativeButton("No") { _, _ ->
                    val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                    Navigation.findNavController(binding.root).navigate(action)
                    onDestroy()
                }
                alert.show()
                countDownTimer.cancel()
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.countdown.text = (millisUntilFinished / 1000).toString()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        println("HELLO DESTORY")
    }

    private fun disableOptions() {

        for (button in buttonArray) {
            button.isClickable = false
        }
    }
}
