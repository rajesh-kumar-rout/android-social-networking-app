package com.example.socialnetworking.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.ui.login.LoginActivity
import com.example.socialnetworking.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.shouldNavigateLoginActivityLiveData.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.decideLandingActivity()
    }
}