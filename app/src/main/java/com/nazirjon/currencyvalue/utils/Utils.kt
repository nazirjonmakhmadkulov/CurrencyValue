package com.desiredsoftware.currencywatcher.utils

import com.desiredsoftware.currencywatcher.data.ValCurs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// Метод преобразовать даты для API
fun convertDateFormatForApiCall(date: Calendar) : String {
    var day = date.get(Calendar.DAY_OF_MONTH).toString()
    var month = (date.get(Calendar.MONTH)+1).toString()  // The day in the month starts from 0
    val year = date.get(Calendar.YEAR).toString()
    if (day.length==1){
        day = StringBuilder(day).insert(0, "0").toString()
    }
    if (month.length==1) {
        month = StringBuilder(month).insert(0, "0").toString()
    }
    return day
            .plus("/")
            .plus(month)
            .plus("/")
            .plus(year)
}

// Метод строка то дата
fun parseStringToDate(stringToParse: String) : Calendar {
    val pattern = "dd/MM/yyyy"
    val dateFormat = SimpleDateFormat(pattern)
    val calendarDate : Calendar = Calendar.getInstance()
    calendarDate.time = dateFormat.parse(stringToParse)

    return calendarDate
}

// Метод получить дни в прошлом месяце
fun getDaysLastMonth() : ArrayList<String> {
    val dateList : ArrayList<String> = ArrayList()
    val currentDate = Calendar.getInstance()
    val dateForDaysInMonthComputing = Calendar.getInstance()
    val daysOnPreviousMonth : Int
    dateForDaysInMonthComputing.add(Calendar.MONTH, -1)
    daysOnPreviousMonth = dateForDaysInMonthComputing.getActualMaximum(Calendar.DAY_OF_MONTH)

    for (i in 0..(daysOnPreviousMonth-1)) {
        dateList.add(convertDateFormatForApiCall(currentDate))
        currentDate.add(Calendar.DAY_OF_MONTH, -1)
    }
    return dateList
}

// Метод получить значение валюты по коду
fun getCurrencyValueByCharCode(currencyCharCode: String, responseValCurs: ValCurs) : Double? {
    responseValCurs.valCurs?.forEach{
        if (it.charCode.equals(currencyCharCode)){
            return it.value?.replace(",", ".")?.toDouble()
        }
    }
    return 0.0
}


