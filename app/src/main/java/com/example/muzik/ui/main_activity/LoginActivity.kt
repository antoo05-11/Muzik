package com.example.muzik.ui.main_activity

 import android.content.Intent
 import android.os.Bundle
 import android.provider.Telephony.Mms.Intents
 import android.view.Menu
import android.view.MenuItem
 import android.view.View
 import android.widget.Button
 import android.widget.EditText
 import android.widget.TextView
 import android.widget.Toast
 import androidx.appcompat.app.AppCompatActivity
 import com.example.muzik.R
 import com.example.muzik.databinding.ActivityLoginBinding


class LoginActivity: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val et_username: EditText = findViewById(R.id.username)
        val et_password: EditText = findViewById(R.id.password)
        val loginBtn: Button = findViewById(R.id.loginbtn)
        val signUpBtn: Button = findViewById(R.id.signupbtn)

        loginBtn.setOnClickListener {
            val username = et_username.text
            val password = et_password.text
            //TODO: Check database
            Toast.makeText(this@LoginActivity, username, Toast.LENGTH_LONG).show()
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}