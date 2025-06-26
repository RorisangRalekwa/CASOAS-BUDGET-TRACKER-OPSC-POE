package com.example.budgettracker10

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class Expenses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        val enterButton = findViewById<Button>(R.id.button7)
        val nextButton = findViewById<Button>(R.id.button8)
        val viewButton = findViewById<Button>(R.id.buttonView)
        val itemInput = findViewById<EditText>(R.id.ExpenseItems)
        val amountInput = findViewById<EditText>(R.id.ExpensesAmount)
        val viewText = findViewById<TextView>(R.id.viewText)
        val categorySpinner = findViewById<Spinner>(R.id.CategorySpinner)


        val userId = intent.getIntExtra("USER_ID", -1)


        val categories = arrayOf("Debit order", "Entertainment", "Utilities", "Groceries")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        val db = BudgetTrackerDatabase.getDatabase(this)

        enterButton.setOnClickListener {
            val category = categorySpinner.selectedItem.toString()
            val item = itemInput.text.toString()
            val amount = amountInput.text.toString()

            if (item.isNotEmpty() && amount.isNotEmpty()) {
                if (userId != -1) {
                    val expense = Expense(
                        userId = userId,
                        category = category,
                        item = item,
                        amount = amount
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        db.expenseDao().insertExpense(expense)
                    }

                    Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show()
                    itemInput.text.clear()
                    amountInput.text.clear()
                } else {
                    Toast.makeText(this, "User not identified!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val expenses = db.expenseDao().getExpensesByUser(userId)
                val displayText = StringBuilder()

                for (expense in expenses) {
                    displayText.append("Category: ${expense.category}\nItem: ${expense.item}\nAmount: ${expense.amount}\n\n")
                }

                withContext(Dispatchers.Main) {
                    viewText.text = if (expenses.isNotEmpty()) displayText.toString() else "No expenses found."
                }
            }
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, BudgetGoals::class.java)
            intent.putExtra("USER_ID", userId) // Pass along
            startActivity(intent)
        }
    }
}
