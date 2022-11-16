package com.naulian.anhance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naulian.anhance.databinding.ActivityMainBinding
import com.naulian.anhance.precis.Binding

class MainActivity : AppCompatActivity() {
    private val binding = Binding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.assign(ActivityMainBinding.inflate(layoutInflater))
        setContentView(binding().root)

        binding {
            btnMain.onClick {
                txtMain.text = ""
            }
        }
    }
}