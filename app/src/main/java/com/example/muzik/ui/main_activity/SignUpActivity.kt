package com.example.muzik.ui.main_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muzik.R
import com.example.muzik.api_controller.MuzikAPI
import com.example.muzik.api_controller.RetrofitHelper
import com.example.muzik.data_model.retrofit_model.response.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class SignUpActivity: AppCompatActivity() {

    val tag: String = "tagne2"
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i(tag, "onCreate")
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
            val fullName = et_fullname.text.toString()
            val email = et_email.text.toString()
            val phoneNumberText = et_phone.text.toString()
            val phoneNumber: Int = phoneNumberText.toInt()
            val dateOfBirthText = et_date_of_birth.text.toString()
            val dateOfBirth: Date? = SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirthText)
            val password = et_password.text.toString()
            val confirmPassword = et_confirmPassword.text.toString()

            if(fullName.trim().isEmpty() || email.trim().isEmpty() || phoneNumberText.trim().isEmpty()
                || dateOfBirthText.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty())
            {
                Toast.makeText(this@SignUpActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()

            } else if(!password.equals(confirmPassword)) {
                Toast.makeText(this@SignUpActivity, "Passwords are not matching", Toast.LENGTH_SHORT).show()
            } else {
                if (dateOfBirth != null) {
                    Log.i(tag, "dateOfBirth != null")
                    performSignUp(fullName, email, phoneNumber, dateOfBirth, password)
                }

            }
        }
    }
    private fun performSignUp(fullName: String, email: String, phoneNumber: Int, dateOfBirth: Date, password: String) {
        val signUpApiService = RetrofitHelper.getInstance().create(MuzikAPI::class.java)

        Log.i(tag, "performSignUp")
        signUpApiService.signUp(fullName, email, phoneNumber, dateOfBirth, password)
            .enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    Log.i(tag, "response.isSuccessful")
                    val signUpResponse: SignUpResponse? = response.body()
                    if (signUpResponse != null) {
                        if (signUpResponse.success) {
                            Log.i(tag, "signUpResponse.success")
                            Toast.makeText(this@SignUpActivity, "Register successfully!", Toast.LENGTH_SHORT).show()

                            //Sử dụng Handler để chuyển hướng sau 5 giây
                            val handler = Handler()
                            handler.postDelayed({
                                Log.i(tag, "handle")
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }, 5000)
                        }
                    }

                } else {
                    Toast.makeText(this@SignUpActivity, "Error in sign up", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

                Log.i(tag, "onFailure")
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "ondestroy")
    }
}