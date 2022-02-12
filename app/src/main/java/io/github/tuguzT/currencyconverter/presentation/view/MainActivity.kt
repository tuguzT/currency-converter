package io.github.tuguzT.currencyconverter.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.tuguzT.currencyconverter.databinding.ActivityMainBinding
import io.github.tuguzT.currencyconverter.presentation.viewmodel.MainActivityModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val accountViewModel: MainActivityModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
