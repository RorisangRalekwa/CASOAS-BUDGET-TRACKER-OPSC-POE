package com.example.budgettracker10

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.budgettracker10.BudgetTrackerDatabase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton = findViewById<Button>(R.id.SignupButton)
        val loginRedirect = findViewById<Button>(R.id.SignupToLogin)

        signUpButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.SignupName).text.toString()
            val email = findViewById<EditText>(R.id.SignupEmail).text.toString()
            val password = findViewById<EditText>(R.id.SignupPassword).text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(name = name, email = email, password = password)
                val db = BudgetTrackerDatabase.getDatabase(this)

                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().insertUser(user)
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "User sign-up successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@MainActivity, LoginPage::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
