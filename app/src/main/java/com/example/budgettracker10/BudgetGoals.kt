package com.example.budgettracker10

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class BudgetGoals : AppCompatActivity() {
    private lateinit var amountInput: EditText
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var enterButton: Button

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goals)

        amountInput = findViewById(R.id.editTextAmount)
        startDateInput = findViewById(R.id.editTextStartDate)
        endDateInput = findViewById(R.id.editTextEndDate)
        enterButton = findViewById(R.id.button9)

        startDateInput.setOnClickListener {
            showDatePickerDialog(startDateInput)
        }

        endDateInput.setOnClickListener {
            showDatePickerDialog(endDateInput)
        }

        enterButton.setOnClickListener {
            val amount = amountInput.text.toString()
            val startDate = startDateInput.text.toString()
            val endDate = endDateInput.text.toString()

            if (amount.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                // TODO: Save the goal to the database here
                Toast.makeText(this, "Goal saved: $amount from $startDate to $endDate", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        val analysisButton = findViewById<Button>(R.id.analysisbutton)
        analysisButton.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            editText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }
}
