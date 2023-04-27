package com.naulian.anhance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naulian.anhance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            val url = "https://firebasestorage.googleapis.com/v0/b/atoz-cloud.appspot.com/o/flashcards%2FLevel%203%2Fimage.jpg?alt=media&token=29b319f7-a459-4a53-8ce2-bbe90d542885"
            imageView.loadWithGlide(url)

            btnMain.onClick {
                txtMain.text = ""
            }
        }
    }
}