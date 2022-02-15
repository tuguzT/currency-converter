package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.ActivityMainBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.viewmodel.MainActivityModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.run {
            setContentView(root)

            swapButton.setOnClickListener {
                window.decorView.clearFocus()

                targetCodeButton.text =
                    baseCodeButton.text.also { baseCodeButton.text = targetCodeButton.text }
                viewModel.targetCode =
                    viewModel.baseCode.also { viewModel.baseCode = viewModel.targetCode }

                targetCodeResult.text = when {
                    baseCodeInput.text.isNullOrBlank() -> getString(R.string.decimal_input_hint)
                    else -> ""
                }
            }

            baseCodeInput.doOnTextChanged { _, _, _, _ ->
                targetCodeResult.text = when {
                    baseCodeInput.text.isNullOrBlank() -> getString(R.string.decimal_input_hint)
                    else -> ""
                }
            }

            baseCodeButton.setOnClickListener {
                lifecycleScope.launch {
                    val codes = viewModel.getSupportedCodes()
                    handleCurrency(codes) {
                        viewModel.baseCode = it
                        baseCodeButton.text = it.code
                    }
                }
            }

            targetCodeButton.setOnClickListener {
                lifecycleScope.launch {
                    val codes = viewModel.getSupportedCodes()
                    handleCurrency(codes) {
                        viewModel.targetCode = it
                        targetCodeButton.text = it.code
                    }
                }
            }

            convertButton.setOnClickListener {
                val handler = CoroutineExceptionHandler { _, throwable ->
                    when (throwable) {
                        is NumberFormatException -> {
                            val text = {
                                val resId = when {
                                    baseCodeInput.text.isNullOrBlank() -> R.string.empty_input
                                    else -> R.string.decimal_input
                                }
                                getString(resId)
                            }
                            snackbarShort(root, text).show()
                        }
                        is IllegalStateException -> {
                            val text = {
                                val resId = when (viewModel.baseCode) {
                                    null -> R.string.base_must_be_specified
                                    else -> R.string.target_must_be_specified
                                }
                                getString(resId)
                            }
                            snackbarShort(root, text).show()
                        }
                    }
                }
                lifecycleScope.launch(handler) {
                    val amount = baseCodeInput.text.toString().toDouble()
                    val conversionResult = viewModel.convert(amount)
                    targetCodeResult.text = conversionResult.result.toString()
                }
            }
        }
    }

    private fun handleCurrency(codes: List<SupportedCode>, handler: (SupportedCode) -> Unit) {
        val dialog = kotlin.run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.choose_currency))
            val items = codes.map(SupportedCode::code).toTypedArray()
            builder.setItems(items) { _, index -> handler(codes[index]) }
            builder.create()
        }
        dialog.show()
    }
}
