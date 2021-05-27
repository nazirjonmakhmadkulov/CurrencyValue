package com.nazirjon.currencyvalue.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.desiredsoftware.currencywatcher.data.api.RemoteService
import com.desiredsoftware.currencywatcher.utils.*
import com.nazirjon.currencyvalue.R
import com.nazirjon.currencyvalue.utils.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class NotificationWatchWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
        workerParams
) {
    val context : Context = context
    private lateinit var sharedPreference: SharedPreference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val remoteService: RemoteService = RemoteService()
        val dateToday : String = convertDateFormatForApiCall(Calendar.getInstance())
        val currencyCharCode = inputData.getString(CURRENCY_CHAR_CODE_INPUT_DATA_WORKER)

        val observable = remoteService.serviceBuilder.getCurrenciesForDate(dateToday)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            valCursList ->
                            sharedPreference = SharedPreference(context)
                            var boundaryValue: Float = sharedPreference.getBoundaryValue()

                            if (currencyCharCode?.let { getCurrencyValueByCharCode(it, valCursList) }!! > boundaryValue && boundaryValue > 0.0f) {
                                    var newMessageNotification = Notification.Builder(context, CHANNEL_ID)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle(context.getString(R.string.notification_title))
                                            .setContentText("Курс ${currencyCharCode} превысил значение ${boundaryValue} RUB")
                                    var notificationManager = (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val name = CHANNEL_NAME
                                    val descriptionText = CHANNEL_DESCRIPTION
                                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                                    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                                        description = descriptionText
                                    }
                                    notificationManager.createNotificationChannel(channel)
                                }
                                    with(NotificationManagerCompat.from(context)) {
                                        notificationManager.notify(1, newMessageNotification.build())
                                    }
                                }
                        },
                        { throwable ->
                            Log.d("Work manager - Rx", "Error with api method call on the Worker: ${throwable.printStackTrace()}")
                        }
                )
        return Result.success()
    }
 }