package com.example.countriesandflagsquiz.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesandflagsquiz.models.CountriesFlagsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountriesAndFlagsViewModel :ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null
    private val CountriesAndFlagsApiService =
        com.example.countriesandflagsquiz.data.entities.CountriesAndFlagsApiService()

    val countriesAndFlags = MutableLiveData<CountriesFlagsModel?>()
    val error = MutableLiveData<Boolean?>()

    fun loadData(){
        compositeDisposable = CompositeDisposable()

        compositeDisposable?.add(CountriesAndFlagsApiService.getData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleResults(it)
            },{
                handleError(it)
            })
        )
    }

    private fun handleResults(weatherModel: CountriesFlagsModel){
        countriesAndFlags.value = weatherModel
        error.value = false
    }

    private fun handleError(throwable: Throwable){
        error.value = true
        println(throwable.toString())
    }
}