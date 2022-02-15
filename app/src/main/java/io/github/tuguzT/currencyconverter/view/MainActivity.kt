package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.ActivityMainBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.viewmodel.MainActivityModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    companion object {
        private val LOG_TAG = MainActivity::class.simpleName
    }

    private val viewModel: MainActivityModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.run {
            setContentView(root)

            swapButton.setOnClickListener {
                window.decorView.clearFocus()

                viewModel.swapCodes()
                baseCodeButton.text = viewModel.baseCode?.code
                targetCodeButton.text = viewModel.targetCode?.code

                resetResult()
            }

            baseCodeInput.doOnTextChanged { _, _, _, _ -> resetResult() }

            baseCodeButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.getSupportedCodes().handleError { supportedCodes ->
                        showCurrencies(supportedCodes) {
                            viewModel.baseCode = it
                            baseCodeButton.text = it.code
                        }
                    }
                }
            }

            targetCodeButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.getSupportedCodes().handleError { supportedCodes ->
                        showCurrencies(supportedCodes) {
                            viewModel.targetCode = it
                            targetCodeButton.text = it.code
                        }
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
                    viewModel.convert(amount).handleError {
                        targetCodeResult.text = it.toString()
                        snackbarShort(root) { getString(R.string.convert_success) }.show()
                    }
                }
            }
        }
    }

    private fun resetResult(): Unit = binding.run {
        targetCodeResult.text = when {
            baseCodeInput.text.isNullOrBlank() -> getString(R.string.decimal_input_hint)
            else -> ""
        }
    }

    private fun <T> ApiResponse<T>.handleError(successHandler: (T) -> Unit): Unit = when (this) {
        is NetworkResponse.Success -> successHandler(body)
        is NetworkResponse.ServerError -> {
            val message = getString(R.string.error_api)
            Log.e(LOG_TAG, "$message: ${body?.type}", error)
            snackbarShort(binding.root) { message }.show()
        }
        is NetworkResponse.NetworkError -> {
            val message = getString(R.string.error_network)
            Log.e(LOG_TAG, message, error)
            snackbarShort(binding.root) { message }.show()
        }
        is NetworkResponse.UnknownError -> {
            val message = getString(R.string.error_unknown)
            Log.e(LOG_TAG, message, error)
            snackbarShort(binding.root) { message }.show()
        }
    }

    private fun showCurrencies(codes: List<SupportedCode>, listener: (SupportedCode) -> Unit) {
        val dialog = kotlin.run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.choose_currency))
            val items = codes.map(SupportedCode::code).toTypedArray()
            builder.setItems(items) { _, index -> listener(codes[index]) }
            builder.create()
        }
        dialog.show()
    }
}
