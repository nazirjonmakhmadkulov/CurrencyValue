package com.nazirjon.currencyvalue.utils

import android.content.Context
import android.content.SharedPreferences
class SharedPreference(context: Context) {

    private val PREFS_NAME = "CURRENCY_VALUE"
    private val PREFS_BOUNDARY_VALUE = "BOUNDARY_VALUE"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Метод сохранение данные
    fun saveBoundaryValue(boundaryValue: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putFloat(PREFS_BOUNDARY_VALUE, boundaryValue.toFloat()).apply()
    }

    // Метод получение данные
    fun getBoundaryValue(): Float {
        return sharedPref.getFloat(PREFS_BOUNDARY_VALUE, 0.0f)
    }
}
