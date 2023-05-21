package com.example.countriesandflagsquiz.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.example.countriesandflagsquiz.models.CountriesFlagsModel
import com.example.countriesandflagsquiz.randomFlags
import com.example.countriesandflagsquiz.viewmodels.CountriesAndFlagsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GuessFlag : Fragment() {
    private var _binding: FragmentGuessFlagBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesAndFlagsViewModel
    private var score = 0
    private var condition = false
    lateinit var countDownTimer : CountDownTimer
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var model : CountriesFlagsModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGuessFlagBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this)[CountriesAndFlagsViewModel::class.java]
        viewModel.loadData()

        val progressDialog = ProgressDialog.show(requireContext(), "Bekleyin", "İndiriliyor...", true)
        viewModel.countriesAndFlags.observe(viewLifecycleOwner) { counrty ->
            counrty.let {
                model = it!!
            }
            progressDialog.dismiss()
            startTimer()
            loadNewQuestion()
        }


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_layout)
        val okButton = dialog.findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener {
            // Do something when the user clicks the button
            startTimer()
            loadNewQuestion()
            dialog.dismiss()
        }
        dialog.show()*/
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpListener(coutryName :String){
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
            if (checkAnswer(binding.aOption.text.toString(),coutryName)){
                binding.aOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.aOption.setBackgroundResource(R.drawable.wrong_answer)
           // binding.aOption.setBackgroundResource(R.drawable.buttun_design)
        }
        binding.bOption.setOnClickListener {
            if (checkAnswer(binding.bOption.text.toString(),coutryName)){
                binding.bOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.bOption.setBackgroundResource(R.drawable.wrong_answer)
           // binding.bOption.setBackgroundResource(R.drawable.buttun_design)
        }
        binding.cOption.setOnClickListener {
            if (checkAnswer(binding.cOption.text.toString(),coutryName)){
                binding.cOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.cOption.setBackgroundResource(R.drawable.wrong_answer)
            //binding.cOption.setBackgroundResource(R.drawable.buttun_design)
        }
        binding.dOption.setOnClickListener {
            if (checkAnswer(binding.dOption.text.toString(),coutryName)){
                binding.dOption.setBackgroundResource(R.drawable.correct_answer)
            }
            else binding.dOption.setBackgroundResource(R.drawable.wrong_answer)
            //binding.dOption.setBackgroundResource(R.drawable.buttun_design)
        }
    }

    @SuppressLint("SetTextI18n")
    fun checkAnswer(answer : String, coutryName:String):Boolean{

        if (answer==coutryName){
            score += 1
            condition = true
            binding.questNo.text = "Correct Answers: $score"

            countDownTimer.cancel()
            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Answer correct")
            alert.setPositiveButton("Next") { _, _ ->
                binding.aOption.setBackgroundResource(R.drawable.buttun_design)
                binding.bOption.setBackgroundResource(R.drawable.buttun_design)
                binding.cOption.setBackgroundResource(R.drawable.buttun_design)
                binding.dOption.setBackgroundResource(R.drawable.buttun_design)
                loadNewQuestion()
            }


            alert.show()
        }
        else{
            condition = false
            binding.questNo.text = "Correct Answers: $score"

            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Correct Answer: $coutryName \n Score: $score \n Game Over")
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
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle("Time is over")
                alert.setMessage("Try Again?")
                alert.setPositiveButton("Yes") { _, _ ->
                    startTimer()
                    loadNewQuestion()
                }
                alert.setNegativeButton("No") { _, _ ->
                    Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_LONG).show()
                    val action = GuessFlagDirections.actionGuessFlagToMainScreen()
                    Navigation.findNavController(binding.root).navigate(action)
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

    fun loadNewQuestion(){
        var coutryName= ""
            val countries = randomFlags()

            binding.aOption.text = model.data[countries.elementAtOrNull(0)!!].name.toString()
            binding.bOption.text = model.data[countries.elementAtOrNull(1)!!].name.toString()
            binding.cOption.text = model.data[countries.elementAtOrNull(2)!!].name.toString()
            binding.dOption.text = model.data[countries.elementAtOrNull(3)!!].name.toString()

            val choseRandomAnswer = model.data[countries.random()]
            GlideToVectorYou.justLoadImage(requireActivity(), Uri.parse(choseRandomAnswer.flag.toString()), binding.flagImage)
            coutryName= choseRandomAnswer.name.toString()

            countDownTimer.cancel()
            countDownTimer.start()
            setUpListener(coutryName)
        }
    }
