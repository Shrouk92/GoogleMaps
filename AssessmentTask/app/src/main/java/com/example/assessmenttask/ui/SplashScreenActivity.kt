package com.example.assessmenttask.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assessmenttask.R
import com.example.assessmenttask.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgSp.alpha=0f
        binding.imgSp.animate().setDuration(1000).alpha(1f).withEndAction(Runnable {
            val i=Intent(this,MapsActivity::class.java)
            startActivity(i)
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom)
            finish()
        })







    }
}