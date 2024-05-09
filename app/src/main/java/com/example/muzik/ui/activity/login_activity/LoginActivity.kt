package com.example.muzik.ui.activity.login_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.muzik.data_model.api_model.request.LoginRequest
import com.example.muzik.data_model.standard_model.User
import com.example.muzik.databinding.ActivityLoginBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.activity.main_activity.MainActivity
import com.example.muzik.ui.activity.profile_activity.ProfileActivity
import com.example.muzik.ui.activity.sign_up_activity.SignUpActivity
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usernameInputText = binding.username
        val passwordInputText = binding.password
        val loginBtn = binding.loginbtn
        val signUpBtn = binding.signInButton

        binding.backButton.setOnClickListener {
            finish()
        }

        loginBtn.setOnClickListener {
            val username = usernameInputText.text.toString()
            val password = passwordInputText.text.toString()

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val loginRequest = LoginRequest(username = username, password = password)
            performLogin(loginRequest)
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()

        if (SharedPrefManager.getInstance(this).isLoggedIn
            && SharedPrefManager.getInstance(this).isAccessTokenExpired
        ) {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun performLogin(loginRequest: LoginRequest) {
        lifecycleScope.launch {
            val loginResponse = MainActivity.muzikAPI.userLogin(loginRequest = loginRequest)
            loginResponse.body().let {
                if (it != null) {
                    val accessToken = it.accessToken
                    SharedPrefManager.getInstance(this@LoginActivity)
                        .saveAccessToken(accessToken)
                    SharedPrefManager.getInstance(applicationContext)
                        .saveUser(User.build(it))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Wrong password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
