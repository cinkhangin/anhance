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

        binding.apply {
            btnMain.setOnClickListener {
                if(isInternetAvailable){
                    txtMain.text = "connected"
                }else txtMain.text = "disconnected"
            }
        }

    }
}