package com.example.countriesandflagsquiz.ui.views

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.countriesandflagsquiz.CircularProgressBar
import com.example.countriesandflagsquiz.databinding.FragmentQuestionDisplayBinding


class QuestionDisplay : Fragment() {
    private var _binding: FragmentQuestionDisplayBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar2: CircularProgressBar
    private lateinit var countDownTimer: CountDownTimer

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

        val countdownDuration = 60000L // Geri sayım süresi (60 saniye)

        countDownTimer = object : CountDownTimer(countdownDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                progressBar2.setProgress(secondsRemaining)
            }

            override fun onFinish() {
                progressBar2.setProgress(0)
                // Geri sayım tamamlandığında yapılacak işlemler
            }
        }

        countDownTimer.start()


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel() // Geri sayımı iptal et
    }

}