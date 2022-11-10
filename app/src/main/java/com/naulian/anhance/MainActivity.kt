package com.naulian.anhance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naulian.anhance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnMain.onClick {
                val duration = hourOf(3) + minuteOf(2) + secondOf(42)
                txtMain.text = duration.formatDuration()
            }
        }
    }
}