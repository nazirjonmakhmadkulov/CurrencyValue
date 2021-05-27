package com.desiredsoftware.currencywatcher.utils

import java.util.concurrent.TimeUnit

const val EXCHANGE_RATE_USD = "USD"

// Настройки для worker
val REPEAT_TIME_UNIT_FOR_WORKER = TimeUnit.MINUTES
val REPEAT_INTERVAL_WORKER = 15L
val CURRENCY_CHAR_CODE_INPUT_DATA_WORKER = "CURRENCY"
val UNIQUE_WORK_NAME_WORKER = "COMPARING_WATCH_WORK"

val DELAY_TIME_UNIT_WORKER = TimeUnit.SECONDS
const val DELAY_INTERVAL_WORKER = 5L

// Настройки для notifications
const val CHANNEL_ID: String = "MY_CHANNEL_ID"
const val CHANNEL_NAME: String = "Channel for boundary value notifications"
const val CHANNEL_DESCRIPTION: String = "Boundary value was exceeded"




