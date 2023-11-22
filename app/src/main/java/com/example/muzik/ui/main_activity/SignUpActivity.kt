package com.example.muzik.ui.main_activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muzik.R

class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val et_fullname: EditText = findViewById(R.id.fullname)
        val et_email: EditText = findViewById(R.id.email)
        val et_phone: EditText = findViewById(R.id.phone)
        val et_date_of_birth: EditText = findViewById(R.id.birthdate)
        val et_password: EditText = findViewById(R.id.password)
        val et_confirmPassword: EditText = findViewById(R.id.confirmPass)
        val signUpBtn: Button = findViewById(R.id.signupbtn)

        signUpBtn.setOnClickListener{
            val fullname = et_fullname.text
            val email = et_email.text
            val phone = et_phone.text
            val date_of_birth = et_date_of_birth.text
            val password = et_password.text
            val confirmPassword = et_confirmPassword.text

            if(fullname.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()
                || date_of_birth.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty())
            {
                Toast.makeText(this@SignUpActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()

            } else if(!password.equals(confirmPassword)) {
                Toast.makeText(this@SignUpActivity, "Passwords are not matching", Toast.LENGTH_SHORT).show()
            } else {
                //TODO: send to database
                Toast.makeText(this@SignUpActivity, "Register successfully!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}