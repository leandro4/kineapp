package com.gon.kineapp.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var listener: DateListener? = null

    interface DateListener {
        fun onDateSelected(date: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(context!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val dayString = if (day < 10) "0" + day else day
        val monthString = if (month < 10) "0" + (month+1) else (month+1)
        listener?.onDateSelected(String.format("%d-%s-%s", year, monthString, dayString))
    }

}