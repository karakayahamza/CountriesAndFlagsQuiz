package com.example.countriesandflagsquiz.ui.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.helpers.getHighScore
import com.example.countriesandflagsquiz.helpers.loadNewQuestion
import com.example.countriesandflagsquiz.helpers.saveHighScore
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel


class GuessFlag : Fragment() {
    private var _binding: FragmentGuessFlagBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryViewModel: CountriesAndFlagsViewModel
    private var score = 0
    lateinit var countDownTimer : CountDownTimer
    private lateinit var model : CountriesFlagsModel
    private var maxScore : Int = 0


    private lateinit var decorView: ViewGroup
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


    }
    @SuppressLint("ResourceAsColor")
    private fun setUpListener(countryName :String){
       /* binding.aOption.setOnClickListener {
            if (handleAnswer(binding.aOption.text.toString(),countryName)){
                nextQuestion()
            }
            else showGameOverDialog(countryName)
        }
        binding.bOption.setOnClickListener {
            if (handleAnswer(binding.bOption.text.toString(),countryName)){
                nextQuestion()
            }
            else showGameOverDialog(countryName)
        }
        binding.cOption.setOnClickListener {
            if (handleAnswer(binding.cOption.text.toString(),countryName)){
                nextQuestion()
            }
            else showGameOverDialog(countryName)
        }
        binding.dOption.setOnClickListener {
            if (handleAnswer(binding.dOption.text.toString(),countryName)){
                nextQuestion()
            }
            else showGameOverDialog(countryName)
        }*/

        val buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)
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
        flashGreenLights()
        setUpListener(loadNewQuestion(model,binding,countDownTimer))
    }


    private fun showGameOverDialog(capitalName:String) {
        countDownTimer.cancel()
        disableOptions()
        flashRedLights()
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
                    setUpListener(com.example.countriesandflagsquiz.helpers.loadNewQuestion(model,binding,countDownTimer))
                }
                alert.setNegativeButton("No") { _, _ ->
                    Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_LONG).show()
                    val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                    Navigation.findNavController(binding.root).navigate(action)
                    onDestroy()
                }
                alert.show()
                countDownTimer.cancel()
                Toast.makeText(requireContext(),"Time is Over",Toast.LENGTH_SHORT).show()
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
    }
    private fun disableOptions() {
        val buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)
        for (button in buttonArray) {
            button.isClickable = false
        }
    }
    private fun flashGreenLights() {
        // Left edge
        val leftLights = View(requireContext())
        leftLights.setBackgroundColor(Color.GREEN)
        val leftParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        leftParams.marginStart = 0
        decorView.addView(leftLights, leftParams)

        // Right edge
        val rightLights = View(requireContext())
        rightLights.setBackgroundColor(Color.GREEN)
        val rightParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        rightParams.width = 20
        rightParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        rightParams.marginStart = decorView.width - 20
        decorView.addView(rightLights, rightParams)

        // Top edge
        val topLights = View(requireContext())
        topLights.setBackgroundColor(Color.GREEN)
        val topParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20)
        topParams.topMargin = 0
        decorView.addView(topLights, topParams)

        // Bottom edge
        val bottomLights = View(requireContext())
        bottomLights.setBackgroundColor(Color.GREEN)
        val bottomParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20)
        bottomParams.height = 20
        bottomParams.topMargin = decorView.height - 20
        decorView.addView(bottomLights, bottomParams)


        Handler().postDelayed({
            decorView.removeView(leftLights)
            decorView.removeView(rightLights)
            decorView.removeView(topLights)
            decorView.removeView(bottomLights)
        }, 1000)
    }
    private fun flashRedLights() {
        // Left edge
        val leftLights = View(requireContext())
        leftLights.setBackgroundColor(Color.RED)
        val leftParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        leftParams.marginStart = 0
        decorView.addView(leftLights, leftParams)

        // Right edge
        val rightLights = View(requireContext())
        rightLights.setBackgroundColor(Color.RED)
        val rightParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        rightParams.width = 20
        rightParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        rightParams.marginStart = decorView.width - 20
        decorView.addView(rightLights, rightParams)

        // Top edge
        val topLights = View(requireContext())
        topLights.setBackgroundColor(Color.RED)
        val topParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20)
        topParams.topMargin = 0
        decorView.addView(topLights, topParams)

        // Bottom edge
        val bottomLights = View(requireContext())
        bottomLights.setBackgroundColor(Color.RED)
        val bottomParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20)
        bottomParams.height = 20
        bottomParams.topMargin = decorView.height - 20
        decorView.addView(bottomLights, bottomParams)


        Handler().postDelayed({
            decorView.removeView(leftLights)
            decorView.removeView(rightLights)
            decorView.removeView(topLights)
            decorView.removeView(bottomLights)
        }, 1000)
    }
}
