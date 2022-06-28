package com.gb.android.mystopwatchb.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gb.android.mystopwatchb.databinding.ActivityMainBinding
import com.gb.android.mystopwatchb.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels()

        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        ).launch {
            viewModel.ticker.collect {
                binding.textTime.text = it
            }
        }

        binding.buttonStart.setOnClickListener {
            viewModel.start()
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pause()
        }
        binding.buttonStop.setOnClickListener {
            viewModel.stop()
        }

    }

}