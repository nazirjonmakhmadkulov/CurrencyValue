package com.desiredsoftware.currencywatcher.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.desiredsoftware.currencywatcher.utils.*
import com.nazirjon.currencyvalue.R
import com.nazirjon.currencyvalue.notification.NotificationWatchWorker
import com.nazirjon.currencyvalue.adapter.CurrencyAdapter
import com.nazirjon.currencyvalue.databinding.MainFragmentBinding
import com.nazirjon.currencyvalue.utils.SharedPreference
import com.nazirjon.currencyvalue.utils.isInternetAvailable
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var fragmentViewModel: MainFragmentViewModel
    private lateinit var currencyAdapter: CurrencyAdapter

    private var fragmentBinding : MainFragmentBinding? = null
    private val binding get() = fragmentBinding!!

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        fragmentViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)

        fragmentBinding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPreference = SharedPreference(requireContext())

        fragmentViewModel.boundaryValue.value = sharedPreference.getBoundaryValue()

        if (fragmentViewModel.boundaryValue.value!=0.0f) {
            binding.editTextBoundaryValue.setText(fragmentViewModel.boundaryValue.value.toString())
        }
        binding.button.setOnClickListener {
            getCuurrencyData()
        }
        fragmentViewModel.currencyForShowUSD.value = EXCHANGE_RATE_USD

        binding.editTextBoundaryValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setWorker(REPEAT_TIME_UNIT_FOR_WORKER, REPEAT_INTERVAL_WORKER, DELAY_TIME_UNIT_WORKER, DELAY_INTERVAL_WORKER)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
            ) {
                if (s.isNotEmpty())
                    sharedPreference.saveBoundaryValue(binding.editTextBoundaryValue.text.toString())
            }
        })

        getCuurrencyData()
        return view
    }

    fun getCuurrencyData(){
        if (isInternetAvailable(requireContext())) {
            binding.progressBar.isVisible = true
            fragmentViewModel.getCurrenciesForDateRange(getDaysLastMonth())
        }else{
            val toast = Toast.makeText(requireContext(), context?.getString(R.string.internet_info), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel.currencyResults.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            currencyAdapter = fragmentViewModel.currencyForShowUSD.value?.let { it1 ->
                CurrencyAdapter(requireContext(),
                        it, it1)
            }!!
            binding.recyclerViewExchangeRate.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewExchangeRate.adapter = currencyAdapter
        })

        fragmentViewModel.currencyForShowUSD.observe(viewLifecycleOwner, Observer {
            fragmentViewModel.getCurrenciesForDateRange(getDaysLastMonth())
            setWorker(REPEAT_TIME_UNIT_FOR_WORKER, REPEAT_INTERVAL_WORKER, DELAY_TIME_UNIT_WORKER, DELAY_INTERVAL_WORKER)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    //Метод установка Worker
    fun setWorker(repeatTimeUnit : TimeUnit, repeatInterval : Long, delayTimeUnit : TimeUnit, delayInterval : Long) {
        val inputData: Data = Data.Builder()
                .putString(CURRENCY_CHAR_CODE_INPUT_DATA_WORKER, fragmentViewModel.currencyForShowUSD.value)
                .build()
        val comparingRequest =
                PeriodicWorkRequest.Builder(NotificationWatchWorker::class.java, repeatInterval, repeatTimeUnit)
                        .setInputData(inputData)
                        .setInitialDelay(delayInterval, delayTimeUnit)
                        .build()
        WorkManager
                .getInstance(requireContext())
                .enqueueUniquePeriodicWork(UNIQUE_WORK_NAME_WORKER, ExistingPeriodicWorkPolicy.REPLACE, comparingRequest)
    }
}