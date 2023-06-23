package com.example.countriesandflagsquiz.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.CircularProgressBar
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.TriviaModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.databinding.FragmentQuestionDisplayBinding
import com.example.countriesandflagsquiz.helpers.randomFlags
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel
import com.example.countriesandflagsquiz.ui.viewmodels.TriviaViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


class QuestionDisplay : Fragment() {
    private var _binding: FragmentQuestionDisplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryViewModel: CountriesAndFlagsViewModel
    private lateinit var triviaViewModel: TriviaViewModel
    private lateinit var model : CountriesFlagsModel
    private lateinit var triviaModel: TriviaModel
    private lateinit var results: ArrayList<TriviaModel.Result>

    private lateinit var decorView: ViewGroup
    private var maxScore = 0
    private var score = 0
    private lateinit var buttonArray : Array<Button>
    private lateinit var progressBar2: CircularProgressBar
    private lateinit var countDownTimer: CountDownTimer

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
        _binding = FragmentQuestionDisplayBinding.inflate(inflater, container, false)

        val progressBar: ProgressBar = binding.progressBar
        progressBar.progress = 50 // İlerleme çubuğunu %50'ye ayarlar
        progressBar2 = binding.progressBarCircular

        val apiService = ApiServiceFactory.create()
        val quizRepository = CountryRepository(apiService)


        countryViewModel = CountriesAndFlagsViewModel(quizRepository)

        countryViewModel.loadData()


        triviaViewModel = ViewModelProviders.of(this)[TriviaViewModel::class.java]

        triviaViewModel.loadTriviaData(10,9,"easy","multiple")


        maxScore = getHighScore(requireContext(),
            SHARED_PREFS_FILE_NAME,
            GUESS_FLAG_SCORE_KEY_NAME
        )

        decorView = requireActivity().window.decorView as ViewGroup

        return binding.root
    }

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
        //binding.score.text = "Max Score: $maxScore Correct Answers: $score"
        buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption)


        triviaViewModel.trivia.observe(viewLifecycleOwner){ trivia ->
            trivia.let {
                triviaModel = it!!

                results = triviaModel.results!!

                println("hererererrere")

                println(triviaModel.response_code)


                println(triviaModel.results!![5].type.toString())


                println(triviaModel.results!![0].correct_answer.toString())

                for(i in results){
                    println(i.question.toString())
                }

            }
        }
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
        //binding.score.text = "Max Score: $maxScore Correct Answers: ${++score}"
        setAnswerCheckLight(requireContext(),decorView, Color.GREEN)
        setUpListener(loadNewQuestion(model,binding,countDownTimer))
    }

    private fun showGameOverDialog(capitalName:String) {
        countDownTimer.cancel()
        disableOptions()
        setAnswerCheckLight(requireContext(),decorView, Color.RED)
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

        if (getHighScore(requireContext(),
                SHARED_PREFS_FILE_NAME,
                GUESS_FLAG_SCORE_KEY_NAME
            ) < score) {
            saveHighScore(requireContext(),score,
                SHARED_PREFS_FILE_NAME,
                GUESS_FLAG_SCORE_KEY_NAME
            )
        }
    }

    fun startTimer(){
        val countdownDuration = 5000L // Geri sayım süresi (5 saniye)

        countDownTimer = object : CountDownTimer(countdownDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                progressBar2.setProgress(secondsRemaining)
            }

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
                progressBar2.setProgress(0)
            }
        }.start()
    }

    fun loadNewQuestion(model: CountriesFlagsModel, binding: FragmentQuestionDisplayBinding, countDownTimer: CountDownTimer):String {
        var countryName = ""
        val countries = randomFlags()

        binding.aOption.text = model.data[countries.elementAtOrNull(0)!!].name.toString()
        binding.bOption.text = model.data[countries.elementAtOrNull(1)!!].name.toString()
        binding.cOption.text = model.data[countries.elementAtOrNull(2)!!].name.toString()
        binding.dOption.text = model.data[countries.elementAtOrNull(3)!!].name.toString()

        val choseRandomAnswer = model.data[countries.random()]
        GlideToVectorYou.justLoadImage(binding.root.context as Activity?, Uri.parse(choseRandomAnswer.flag.toString()), binding.flagImage)
        countryName = choseRandomAnswer.name.toString()

        countDownTimer.cancel()
        countDownTimer.start()
        return countryName
    }

    fun setAnswerCheckLight(context: Context, decorView: ViewGroup, color: Int) {
        var setColor = Color.GREEN
        if (color == Color.GREEN){
            setColor = Color.GREEN
        }
        else setColor = Color.RED


        // Left edge
        val leftLights = View(context)
        leftLights.setBackgroundColor(setColor)
        val leftParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        leftParams.marginStart = 0
        decorView.addView(leftLights, leftParams)

        // Right edge
        val rightLights = View(context)
        rightLights.setBackgroundColor(setColor)
        val rightParams = FrameLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        rightParams.width = 20
        rightParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        rightParams.marginStart = decorView.width - 20
        decorView.addView(rightLights, rightParams)

        // Top edge
        val topLights = View(context)
        topLights.setBackgroundColor(setColor)
        val topParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20)
        topParams.topMargin = 0
        decorView.addView(topLights, topParams)

        // Bottom edge
        val bottomLights = View(context)
        bottomLights.setBackgroundColor(setColor)
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
    fun getHighScore(context: Context, SHARED_PREFS_FILE_NAME:String, KEY_NAME:String): Int {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return (sharedPreferences.getInt(KEY_NAME, 0) ?: "") as Int
    }

    fun saveHighScore(context: Context, name: Int, SHARED_PREFS_FILE_NAME:String, KEY_NAME:String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_NAME, name)
        editor.apply()
    }

    private fun disableOptions() {
        for (button in buttonArray) {
            button.isClickable = false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel() // Geri sayımı iptal et
    }
}