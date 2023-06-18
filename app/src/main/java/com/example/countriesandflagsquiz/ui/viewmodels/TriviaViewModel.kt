package com.example.countriesandflagsquiz.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesandflagsquiz.data.model.TriviaModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TriviaViewModel : ViewModel() {

    private val weatherApiServices =
        com.example.countriesandflagsquiz.data.api.ApiServiceTrivia()

    private var compositeDisposable: CompositeDisposable? = null



    val trivia = MutableLiveData<TriviaModel?>()
    val error = MutableLiveData<Boolean?>()

    fun loadTriviaData(amount:Int,category:Int, difficulty:String,type:String){
        compositeDisposable = CompositeDisposable()

        compositeDisposable?.add(
            weatherApiServices.getTrivia(amount,category,difficulty,type)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleResults(it)
            },{
                handleError(it)
            })
        )
    }


    private fun handleResults(countryModel: TriviaModel){
        trivia.value = countryModel
        error.value = false
    }

    private fun handleError(throwable: Throwable){
        error.value = true
        println(throwable.toString())
    }

}