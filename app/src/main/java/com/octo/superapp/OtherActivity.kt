package com.octo.superapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.octo.superapp.databinding.ActivityOtherLayoutBinding

class OtherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}