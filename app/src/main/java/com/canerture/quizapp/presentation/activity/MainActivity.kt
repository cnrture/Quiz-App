package com.canerture.quizapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canerture.quizapp.databinding.ActivityMainBinding
import com.canerture.quizapp.delegation.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
