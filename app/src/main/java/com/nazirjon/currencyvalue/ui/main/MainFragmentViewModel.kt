package com.desiredsoftware.currencywatcher.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desiredsoftware.currencywatcher.data.ValCurs
import com.desiredsoftware.currencywatcher.data.api.RemoteService
import com.desiredsoftware.currencywatcher.utils.parseStringToDate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainFragmentViewModel : ViewModel() {

    private val remoteService: RemoteService = RemoteService()

    var currencyResults: MutableLiveData<ArrayList<ValCurs>> = MutableLiveData()
    var boundaryValue: MutableLiveData<Float> = MutableLiveData()
    var currencyForShowUSD: MutableLiveData<String> = MutableLiveData()

    // Метод получить валюты по диапазона дата
    fun getCurrenciesForDateRange(dateRangeForTheCurrencyReport: ArrayList<String>) {

        val valCursForDateRange : ArrayList<ValCurs> = ArrayList()

        val observable = Observable.fromIterable(dateRangeForTheCurrencyReport)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { dateRequestedItem ->
                    remoteService.serviceBuilder.getCurrenciesForDate(dateRequestedItem)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { currenciesRateResult ->
                                        currenciesRateResult.dateRequested = dateRequestedItem
                                        currenciesRateResult.dateRequestedDate = parseStringToDate(dateRequestedItem)
                                        valCursForDateRange.add(currenciesRateResult)
                                        valCursForDateRange.sortByDescending { it.dateRequestedDate }
                                        currencyResults.value = valCursForDateRange

                                        Log.d("Rx - FragmentViewModel", "onNext called in the getCurrenciesForDate API method for the date requested: ${currenciesRateResult.dateRequested}")
                                    },
                                    { throwable ->
                                        Log.d("Rx - FragmentViewModel", "onError called in the getCurrenciesForDate API method: ${throwable.printStackTrace()}")
                                    })
                }
    }
}