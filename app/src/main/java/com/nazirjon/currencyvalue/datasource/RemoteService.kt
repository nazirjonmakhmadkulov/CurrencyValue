package com.desiredsoftware.currencywatcher.data.api

class RemoteService () {
    val BASE_URL : String = "https://www.cbr.ru/"
    val serviceBuilder = ServiceBuilder.create(BASE_URL)
}