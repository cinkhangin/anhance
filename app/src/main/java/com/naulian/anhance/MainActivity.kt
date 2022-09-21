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

        val duration = 3.year + 122.day + 13.hr + 54.min + 32.sec
        binding.apply {
            txtMain.text = duration.formatDuration()
            btnMain.setOnClickListener {
                if(isInternetAvailable){
                    txtMain.text = "connected"
                }else txtMain.text = "disconnected"
            }
        }

    }
}