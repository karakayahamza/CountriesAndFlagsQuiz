package com.example.countriesandflagsquiz.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountriesAndFlagsViewModel :ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null
    private var compositeDisposable2: CompositeDisposable? = null
    private val CountriesAndFlagsApiService = com.example.countriesandflagsquiz.data.entities.CountriesAndFlagsApiService()

    val countriesAndFlags = MutableLiveData<CountriesFlagsModel?>()
    val countriesCapital = MutableLiveData<CountryCapitalsFlagModel?>()
    val error = MutableLiveData<Boolean?>()
    val error2 = MutableLiveData<Boolean?>()

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

    fun loadCapitalData(){
        compositeDisposable2 = CompositeDisposable()

        compositeDisposable2?.add(CountriesAndFlagsApiService.getCapitalData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleResults2(it)
            },{
                handleError2(it)
            })
        )
    }

    private fun handleResults(countryModel: CountriesFlagsModel){
        countriesAndFlags.value = countryModel
        error.value = false
    }

    private fun handleError(throwable: Throwable){
        error.value = true
        println(throwable.toString())
    }

    private fun handleResults2(countryModel: CountryCapitalsFlagModel){
        countriesCapital.value = countryModel
        error.value = false
    }

    private fun handleError2(throwable: Throwable){
        error.value = true
        println(throwable.toString())
    }
}