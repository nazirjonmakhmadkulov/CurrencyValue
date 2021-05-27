package com.nazirjon.currencyvalue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.desiredsoftware.currencywatcher.data.ValCurs
import com.desiredsoftware.currencywatcher.utils.getCurrencyValueByCharCode
import com.nazirjon.currencyvalue.R
import java.util.ArrayList

class CurrencyAdapter (private val context: Context,
                       private val currenciesInfo: ArrayList<ValCurs>,
                       var currencyCharCode : String) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRequestedDate?.text = context.getString(R.string.requested_date).plus(currenciesInfo[position].dateRequested?.replace("/",".", false))
        holder.tvUpdatedDate?.text = context.getString(R.string.update_date).plus(currenciesInfo[position].dateUpdated)
        holder.tvExchangeRateUSD?.text = context.getString(R.string.currency_unit).plus(currencyCharCode)
        holder.tvExchangeRateRUB?.text = (getCurrencyValueByCharCode(currencyCharCode, currenciesInfo[position]).toString()).plus(context.getString(R.string.rub_currency))
    }

    override fun getItemCount(): Int {
        return currenciesInfo.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRequestedDate: TextView? = null
        var tvUpdatedDate: TextView? = null
        var tvExchangeRateUSD: TextView? = null
        var tvExchangeRateRUB: TextView? = null

        init {
            tvRequestedDate = itemView.findViewById(R.id.tvRequestedDate)
            tvUpdatedDate = itemView.findViewById(R.id.tvUpdatedDate)
            tvExchangeRateUSD = itemView.findViewById(R.id.tvExchangeRateUSD)
            tvExchangeRateRUB = itemView.findViewById(R.id.tvExchangeRateRUB)
        }
    }
}