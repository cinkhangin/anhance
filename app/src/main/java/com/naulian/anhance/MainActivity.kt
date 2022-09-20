package com.naulian.anhance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.naulian.anhance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val a = 1.year + 3.day + 4.hr + 5.sec

        val b = a.toDay
        val c = a.safeToDivide / MINUTE

        val d = a.formatTimer()
        Log.i("MainActivity", "onCreate: $d")

    }
}