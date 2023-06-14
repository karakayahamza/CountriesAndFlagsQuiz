package com.example.countriesandflagsquiz.ui.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.data.api.ApiServiceFactory
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import com.example.countriesandflagsquiz.databinding.FragmentCapitalCityBinding
import com.example.countriesandflagsquiz.helpers.Constants.KEY_NAME
import com.example.countriesandflagsquiz.helpers.Constants.SHARED_PREFS_FILE_NAME
import com.example.countriesandflagsquiz.ui.viewmodels.CountriesAndFlagsViewModel

class CapitalCity : Fragment() {
    private var _binding: FragmentCapitalCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var buttonArray: Array<Button>
    private lateinit var countryViewModel: CountriesAndFlagsViewModel
    private lateinit var model: CountryCapitalsFlagModel
    private val buttonDesign = R.drawable.buttun_design
    private var maxScore: Int = 0
    private var score = 0
    private lateinit var progressBar: ProgressBar
    lateinit var correct: MediaPlayer
    lateinit var gameover: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxScore = getHighScore(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCapitalCityBinding.inflate(inflater, container, false)

        correct = MediaPlayer.create(requireContext(), R.raw.correct)
        gameover = MediaPlayer.create(requireContext(), R.raw.gameover)

        progressBar = ProgressBar(requireContext())
        progressBar.isIndeterminate = true
        progressBar.setBackgroundColor(Color.TRANSPARENT)
        progressBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER

        val rootView = requireActivity().window.decorView.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(progressBar, layoutParams)

        val apiService = ApiServiceFactory.create()
        val quizRepository = CountryRepository(apiService)
        countryViewModel = CountriesAndFlagsViewModel(quizRepository)

        countryViewModel.loadCapitalData()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryViewModel.countriesCapital.observe(viewLifecycleOwner) { country ->
            country.let {
                model = it!!
            }
            progressBar.visibility = View.GONE
            setUpListener(loadNewQuestion(model, binding))
        }
        buttonArray = arrayOf(binding.aOption, binding.bOption, binding.cOption, binding.dOption,binding.joker,binding.joker2)
        binding.score.text = "Max Score: $maxScore Correct Answers: $score"
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpListener(capitalName: String) {
        if (capitalName == "") {
            setOptionsEnabled(false)
        } else {
            setOptionsEnabled(true)
        }

        for (button in buttonArray) {
            button.setOnClickListener {
                if (handleAnswer(button.text.toString(), capitalName)) {
                    button.setBackgroundResource(R.drawable.correct_answer)
                    correct.seekTo(0)
                    correct.start()
                } else{
                    button.setBackgroundResource(R.drawable.wrong_answer)
                    gameover.seekTo(0)
                    gameover.start()
                }
            }
        }

        binding.next.setOnClickListener {
            resetOptions()
            setUpListener(loadNewQuestion(model, binding))
            binding.next.isClickable = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleAnswer(answer: String, capitalName: String): Boolean {
        val result: Boolean
        if (answer == capitalName) {
            binding.score.text = "Max Score: $maxScore Correct Answers: ${++score}"
            binding.next.isClickable = true
            setOptionsEnabled(false)
            result = true
        } else {
            showGameOverDialog(capitalName)
            result = false
        }
        return result
    }

    private fun setOptionsEnabled(enabled: Boolean) {
        for (button in buttonArray) {
            button.isClickable = enabled
        }
    }

    private fun showGameOverDialog(capitalName: String) {
        val action = CapitalCityDirections.actionCapitalCityToMainScreen()

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("<GAME OVER>")
        alertDialogBuilder.setMessage("Correct Answer: $capitalName \nDo you want to try again?")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            score = 0
            setUpListener(loadNewQuestion(model, binding))
            resetOptions()
            gameover.pause()
        }
        alertDialogBuilder.setNegativeButton("No") { _: DialogInterface, _: Int ->
            Navigation.findNavController(binding.root).navigate(action)
            onDestroy()
            gameover.reset()
            correct.reset()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        if (getHighScore(requireContext()) < score) {
            saveHighScore(requireContext(), score)
        }
    }

    private fun resetOptions() {
        for (button in buttonArray) {
            button.setBackgroundResource(buttonDesign)
        }
        setOptionsEnabled(true)
    }

    private fun randomFlags(): MutableSet<Int> {
        val randomNumbers = mutableSetOf<Int>()
        while (randomNumbers.size < 4) {
            randomNumbers.add((1..220).random())
        }
        return randomNumbers
    }

    private fun getHighScore(context: Context): Int {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_NAME, 0)
    }

    private fun saveHighScore(context: Context, name: Int) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_NAME, name)
        editor.apply()
    }

    private fun loadNewQuestion(model: CountryCapitalsFlagModel, binding: FragmentCapitalCityBinding): String {
        val capitalName: String
        val countries = randomFlags()

        binding.aOption.text = model.data[countries.elementAtOrNull(0)!!].capital.toString()
        binding.bOption.text = model.data[countries.elementAtOrNull(1)!!].capital.toString()
        binding.cOption.text = model.data[countries.elementAtOrNull(2)!!].capital.toString()
        binding.dOption.text = model.data[countries.elementAtOrNull(3)!!].capital.toString()

        val random = countries.random()
        val remainingNumbers = ArrayList(countries).apply { remove(random) }

        val chosenRandomAnswer = model.data[random]

        binding.joker.setOnClickListener {
            fiftyPercentJoker(countries.indexOf(remainingNumbers[0]), binding)
            fiftyPercentJoker(countries.indexOf(remainingNumbers[1]), binding)
        }
        binding.countryName.text = chosenRandomAnswer.name
        capitalName = chosenRandomAnswer.capital.toString()

        return capitalName
    }

    private fun fiftyPercentJoker(index: Int, binding: FragmentCapitalCityBinding) {
        when (index) {
            0 -> {
                binding.aOption.isClickable = false
                binding.aOption.setBackgroundResource(R.drawable.used_joker)
            }
            1 -> {
                binding.bOption.isClickable = false
                binding.bOption.setBackgroundResource(R.drawable.used_joker)
            }
            2 -> {
                binding.cOption.isClickable = false
                binding.cOption.setBackgroundResource(R.drawable.used_joker)
            }
            3 -> {
                binding.dOption.isClickable = false
                binding.dOption.setBackgroundResource(R.drawable.used_joker)
            }
            else -> {
                println("Index is not 0, 1, 2 or 3")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onStop() {
        super.onStop()
        progressBar.visibility =View.GONE
    }
}