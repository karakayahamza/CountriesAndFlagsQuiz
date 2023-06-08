package com.example.countriesandflagsquiz.helpers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.countriesandflagsquiz.R
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.databinding.FragmentCapitalCityBinding
import com.example.countriesandflagsquiz.databinding.FragmentGuessFlagBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

fun randomFlags(): MutableSet<Int> {
    val randomNumbers = mutableSetOf<Int>()
    while (randomNumbers.size < 4) {
        randomNumbers.add((1..220).random())
    }
    return randomNumbers
}

fun getHighScore(context: Context,SHARED_PREFS_FILE_NAME:String,KEY_NAME:String): Int {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
    return (sharedPreferences.getInt(KEY_NAME, 0) ?: "") as Int
}

fun saveHighScore(context: Context, name: Int,SHARED_PREFS_FILE_NAME:String,KEY_NAME:String) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putInt(KEY_NAME, name)
    editor.apply()
}

fun loadNewQuestion(model: CountriesFlagsModel, binding: FragmentGuessFlagBinding, countDownTimer: CountDownTimer):String {
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

    fun loadNewQuestion(model: CountryCapitalsFlagModel, binding: FragmentCapitalCityBinding):String{
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
        fiftyPercentJoker(countries.indexOf(remainingNumbers[0]),binding)
        fiftyPercentJoker(countries.indexOf(remainingNumbers[1]),binding)
    }
    binding.countryName.text = choseRandomAnswer.name
    capitalName= choseRandomAnswer.capital.toString()

    return capitalName
}
private fun fiftyPercentJoker(index :Int,binding: FragmentCapitalCityBinding){
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
            println("Index is not 0, 1, 2 or 3")
        }
    }
}
fun setAnswerCheckLight(context: Context,decorView: ViewGroup,color: Int) {
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


