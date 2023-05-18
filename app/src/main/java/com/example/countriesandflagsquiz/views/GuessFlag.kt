package com.example.countriesandflagsquiz.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.randomFlags
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlin.concurrent.timer


class GuessFlag : Fragment() {
    private var _binding: FragmentGuessFlagBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    var score = 0
    var condition = false
    lateinit var countDownTimer : CountDownTimer

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

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_layout)
        val okButton = dialog.findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener {
            // Do something when the user clicks the button
            startTimer()
            loadNewQuestion()
            dialog.dismiss()
        }
        dialog.show()
        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]
        viewModel.loadData()
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

    @SuppressLint("SetTextI18n")
    fun checkAnswer(answer : String, coutryName:String):Boolean{

        if (answer==coutryName){
            Toast.makeText(requireContext(),"Answer Correct",Toast.LENGTH_SHORT).show()
            score += 1
            condition = true
            binding.questNo.text = "Correct Answers: $score"
            loadNewQuestion()
        }
        else{
            Toast.makeText(requireContext(),"Answer Wrong",Toast.LENGTH_SHORT).show()
            condition = false
            binding.questNo.text = "Correct Answers: $score"

            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Game Over \n Score: $score")
            alert.setMessage("Try Again?")
            alert.setPositiveButton("Yes") { _, _ ->
                score = 0
                startTimer()
                loadNewQuestion()
            }
            alert.setNegativeButton("No") { _, _ ->
                val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                Navigation.findNavController(binding.root).navigate(action)
                onDestroy()
            }
            alert.show()
            countDownTimer.cancel()
        }
        return condition
    }

    fun startTimer(){
        condition = true // Flag to track wrong answer
        countDownTimer = object : CountDownTimer(15500, 1000) {
            override fun onFinish() {
               // handler.removeCallbacks(runnable)

                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle("Wrong Answer")
                alert.setMessage("Try Again?")
                alert.setPositiveButton("Yes") { _, _ ->
                    startTimer()
                    loadNewQuestion()
                }
                alert.setNegativeButton("No") { _, _ ->
                    Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_LONG).show()
                    alert.show()
                    val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                    Navigation.findNavController(binding.root).navigate(action)
                    }
                countDownTimer.cancel()
            }
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.countdown.text = (millisUntilFinished / 1000).toString()
            }
        }.start()

        condition = false
        if (condition){
            countDownTimer.cancel()
        }
    }

    fun loadNewQuestion(){
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
}