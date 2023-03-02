package com.example.countriesandflagsquiz.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesandflagsquiz.apis.CountriesAndFlagsApiService
import com.example.countriesandflagsquiz.models.CountriesAndFlagsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountriesAndFlagsViewModel :ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null
    private val CountriesAndFlagsApiService = com.example.countriesandflagsquiz.apis.CountriesAndFlagsApiService()

    val countriesAndFlags = MutableLiveData<CountriesAndFlagsModel?>()
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

    private fun handleResults(weatherModel: CountriesAndFlagsModel){
        countriesAndFlags.value = weatherModel
        error.value = false
    }

    private fun handleError(throwable: Throwable){
        error.value = true
        println(throwable.toString())
    }
}