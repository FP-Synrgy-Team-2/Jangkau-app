package com.example.jangkau.feature.mutation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityFilterMutationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FilterMutationActivity : BaseActivity() {
    private lateinit var binding: ActivityFilterMutationBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardFromDate.root.setOnClickListener {
            showDatePicker(this) { selectedDate ->
                binding.cardFromDate.editTextFromDate.text = selectedDate
            }
        }

        binding.cardToDate.root.setOnClickListener {
            showDatePicker(this) { selectedDate ->
                binding.cardToDate.editTextFromDate.text = selectedDate
            }
        }
    }

    private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.minDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -14)
            }.timeInMillis
        }

        datePickerDialog.show()
    }
}
