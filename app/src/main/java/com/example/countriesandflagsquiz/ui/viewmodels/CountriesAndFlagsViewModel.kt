package com.example.countriesandflagsquiz.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesandflagsquiz.data.model.CountriesFlagsModel
import com.example.countriesandflagsquiz.data.model.CountryCapitalsFlagModel
import com.example.countriesandflagsquiz.data.repository.CountryRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountriesAndFlagsViewModel(private val countryRepository: CountryRepository) :ViewModel() {
    private var compositeDisposable: CompositeDisposable? = null
    private var compositeDisposable2: CompositeDisposable? = null

    val countriesAndFlags = MutableLiveData<CountriesFlagsModel?>()
    val countriesCapital = MutableLiveData<CountryCapitalsFlagModel?>()
    val error = MutableLiveData<Boolean?>()
    val error2 = MutableLiveData<Boolean?>()

    fun loadData(){
        compositeDisposable = CompositeDisposable()

        compositeDisposable?.add(countryRepository.getFlagData()
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

        compositeDisposable2?.add(countryRepository.getCapitalData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleCapitaCity(it)
            },{
                handleErrorCapitalCity(it)
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

    private fun handleCapitaCity(countryModel: CountryCapitalsFlagModel){
        countriesCapital.value = countryModel
        error.value = false
    }

    private fun handleErrorCapitalCity(throwable: Throwable){
        error2.value = true
        println(throwable.toString())
    }
}