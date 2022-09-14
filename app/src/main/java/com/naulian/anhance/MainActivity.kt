package com.naulian.anhance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naulian.anhance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val threeMinutes = 3.min
        val OneHundredYears = 100.year

    }
}