package com.example.budgettracker10

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class Analytics : AppCompatActivity() {

    private lateinit var salaryInput: EditText
    private lateinit var expenseInput: EditText
    private lateinit var goalInput: EditText
    private lateinit var pieChart: PieChart
    private lateinit var analyticsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        salaryInput = findViewById(R.id.monthlySalaryText)
        expenseInput = findViewById(R.id.expensesAmountText)
        goalInput = findViewById(R.id.budgetGoalAmountText)
        pieChart = findViewById(R.id.pieChart)
        analyticsButton = findViewById(R.id.analyticsButton)

        analyticsButton.setOnClickListener {
            val salary = salaryInput.text.toString().toDoubleOrNull()
            val expenses = expenseInput.text.toString().toDoubleOrNull()
            val goal = goalInput.text.toString().toDoubleOrNull()

            if (salary != null && expenses != null && goal != null) {
                showPieChart(salary, expenses, goal)
                showGamificationMessage(salary, expenses)
            } else {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPieChart(salary: Double, expenses: Double, goal: Double) {
        val total = expenses + goal + (salary - expenses - goal)
        val entries = ArrayList<PieEntry>()

        // Calculate percentages
        val expensePercent = (expenses / total) * 100
        val goalPercent = (goal / total) * 100
        val remaining = salary - expenses - goal
        val remainingPercent = (remaining / total) * 100

        // Add entries with amount and percentage
        entries.add(
            PieEntry(
                expenses.toFloat(),
                "Expenses: R${"%.2f".format(expenses)} (${String.format("%.1f", expensePercent)}%)"
            )
        )
        entries.add(
            PieEntry(
                goal.toFloat(),
                "Goal: R${"%.2f".format(goal)} (${String.format("%.1f", goalPercent)}%)"
            )
        )
        if (remaining > 0) {
            entries.add(
                PieEntry(
                    remaining.toFloat(),
                    "Remaining: R${"%.2f".format(remaining)} (${String.format("%.1f", remainingPercent)}%)"
                )
            )
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f
        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelTextSize(12f)
        pieChart.animateY(1000)
        pieChart.invalidate()
    }


    private fun showGamificationMessage(salary: Double, expenses: Double) {
        val message = when {
            expenses < salary * 0.5 -> "🎉 You are on the right track when it comes to saving!"
            expenses >= salary * 0.5 -> "⚠️ You are declining on your monthly savings goal."
            else -> ""
        }

        AlertDialog.Builder(this)
            .setTitle("Financial Feedback")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
