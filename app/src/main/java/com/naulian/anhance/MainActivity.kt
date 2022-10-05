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
            btnMain.setOnClickListener {
                val value = loopForValue(0 until 10, 0) { index, value ->
                    value + index
                }
                val text = loopForString(0 until 10) { "$it = $value, " }
                txtMain.text = text
            }
        }
    }
}