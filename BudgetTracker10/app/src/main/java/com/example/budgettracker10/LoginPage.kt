package com.example.budgettracker10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val loginButton = findViewById<Button>(R.id.EnterLoginButton)
        val emailField = findViewById<EditText>(R.id.LoginEmail)
        val passwordField = findViewById<EditText>(R.id.LoginPassword)

        loginButton.setOnClickListener {
            val enteredEmail = emailField.text.toString()
            val enteredPassword = passwordField.text.toString()

            if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                val db = BudgetTrackerDatabase.getDatabase(this)

                CoroutineScope(Dispatchers.IO).launch {
                    val user = db.userDao().getUser(enteredEmail, enteredPassword)
                    runOnUiThread {
                        if (user != null) {
                            Log.d("LoginDebug", "User found: $user")
                            Toast.makeText(this@LoginPage, "Login successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginPage, Expenses::class.java)
                            intent.putExtra("USER_ID", user.id)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginPage, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
