package com.example.jangkau.feature.mutation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityFilterMutationBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class FilterMutationActivity : BaseActivity() {
    private lateinit var binding: ActivityFilterMutationBinding
    private val calendar = Calendar.getInstance()

    private var fromDate: LocalDate? = null
    private var toDate: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardFromDate.root.setOnClickListener {
            showDatePicker(this) { selectedDate ->
                fromDate = selectedDate
                binding.cardFromDate.editTextFromDate.text = selectedDate.formatToDisplay()
            }
        }

        binding.cardToDate.root.setOnClickListener {
            showDatePicker(this) { selectedDate ->
                toDate = selectedDate
                binding.cardToDate.editTextFromDate.text = selectedDate.formatToDisplay()
            }
        }

        binding.btnApply.setOnClickListener {
            validateAndOpenMutasiActivity()
        }
    }

    private fun showDatePicker(context: Context, onDateSelected: (LocalDate) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(selectedDate)
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

    private fun validateAndOpenMutasiActivity() {
        when {
            fromDate == null -> {
                showToast("Silahkan isi tanggal mulai")
            }
            toDate == null -> {
                showToast("Silahkan isi tanggal akhir")
            }
            fromDate!!.isBefore(toDate) -> {
                showToast("Tanggal mulai harus lebih besar dari tanggal akhir")
            }
            else -> {
                openMutasiActivity(fromDate!!, toDate!!, fromMutationFilter = true)
                finish()
            }
        }
    }

    private fun LocalDate.formatToDisplay(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return this.format(formatter)
    }
}
