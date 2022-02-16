package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.FragmentConverterBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.viewmodel.ConverterViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConverterFragment : Fragment() {
    companion object {
        private val LOG_TAG = ConverterFragment::class.simpleName
    }

    private val viewModel: ConverterViewModel by viewModel()

    private var _binding: FragmentConverterBinding? = null

    // This helper property is only valid between `onCreateView` and `onDestroyView`.
    private inline val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentConverterBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swapButton.setOnClickListener {
            requireActivity().window.decorView.clearFocus()

            viewModel.swapCodes()
            binding.baseCodeButton.text = viewModel.baseCode?.code
            binding.targetCodeButton.text = viewModel.targetCode?.code

            resetResult()
        }

        binding.baseCodeInput.doOnTextChanged { _, _, _, _ -> resetResult() }

        binding.baseCodeButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getSupportedCodes().handleError { supportedCodes ->
                    showCurrencies(supportedCodes) {
                        viewModel.baseCode = it
                        binding.baseCodeButton.text = it.code
                    }
                }
            }
        }

        binding.targetCodeButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getSupportedCodes().handleError { supportedCodes ->
                    showCurrencies(supportedCodes) {
                        viewModel.targetCode = it
                        binding.targetCodeButton.text = it.code
                    }
                }
            }
        }

        binding.convertButton.setOnClickListener {
            val handler = CoroutineExceptionHandler { _, throwable ->
                when (throwable) {
                    is NumberFormatException -> {
                        val text = {
                            val resId = when {
                                binding.baseCodeInput.text.isNullOrBlank() -> R.string.empty_input
                                else -> R.string.decimal_input
                            }
                            getString(resId)
                        }
                        snackbarShort(binding.root, text).show()
                    }
                    is IllegalStateException -> {
                        val text = {
                            val resId = when (viewModel.baseCode) {
                                null -> R.string.base_must_be_specified
                                else -> R.string.target_must_be_specified
                            }
                            getString(resId)
                        }
                        snackbarShort(binding.root, text).show()
                    }
                }
            }
            lifecycleScope.launch(handler) {
                val amount = binding.baseCodeInput.text.toString().toDouble()
                viewModel.convert(amount).handleError {
                    binding.targetCodeResult.text = it.toString()
                    snackbarShort(binding.root) { getString(R.string.convert_success) }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.choose_currency))
            val items = codes.map(SupportedCode::code).toTypedArray()
            builder.setItems(items) { _, index -> listener(codes[index]) }
            builder.create()
        }
        dialog.show()
    }
}
