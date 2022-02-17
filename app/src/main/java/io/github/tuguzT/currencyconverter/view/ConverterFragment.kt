package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.FragmentConverterBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.viewmodel.ConverterViewModel
import io.github.tuguzT.currencyconverter.viewmodel.SupportedCodesViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.navigation.koinNavGraphViewModel

class ConverterFragment : Fragment() {
    companion object {
        const val BASE_CODE_KEY = "base_code"
        const val TARGET_CODE_KEY = "target_code"
    }

    private val viewModel: ConverterViewModel by koinNavGraphViewModel(R.id.nav_graph)
    private val codesViewModel: SupportedCodesViewModel by koinNavGraphViewModel(R.id.nav_graph)

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
            val action = ConverterFragmentDirections.toSupportedCodesList(BASE_CODE_KEY)
            findNavController().navigate(action)
        }

        binding.targetCodeButton.setOnClickListener {
            val action = ConverterFragmentDirections.toSupportedCodesList(TARGET_CODE_KEY)
            findNavController().navigate(action)
        }

        createPopBackStackObserver(BASE_CODE_KEY) {
            viewModel.baseCode = it
            binding.baseCodeButton.text = it.code
        }

        createPopBackStackObserver(TARGET_CODE_KEY) {
            viewModel.targetCode = it
            binding.targetCodeButton.text = it.code
        }

        val convertButtonHandler = CoroutineExceptionHandler { _, throwable ->
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
        binding.convertButton.setOnClickListener {
            val activity = requireActivity() as MainActivity
            activity.showProgress()

            lifecycleScope.launch(convertButtonHandler) {
                val amount = binding.baseCodeInput.text.toString().toDouble()
                if (viewModel.baseCode != null && viewModel.baseCode == viewModel.targetCode) {
                    withContext(Dispatchers.Main) {
                        binding.targetCodeResult.text = amount.toString()
                        activity.hideProgress()
                        snackbarShort(binding.root) { getString(R.string.convert_success) }.show()
                    }
                    return@launch
                }
                viewModel.getRate().handle {
                    binding.targetCodeResult.text = (it.rate * amount).round(6).toString()
                    activity.hideProgress()
                    snackbarShort(binding.root) { getString(R.string.convert_success) }.show()
                }
            }
        }
    }

    private fun createPopBackStackObserver(key: String, observer: (SupportedCode) -> Unit) {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        val liveData = savedStateHandle?.getLiveData<String>(key)

        liveData?.observe(viewLifecycleOwner) { code ->
            lifecycleScope.launch {
                val result = codesViewModel.getSupportedCodes().find { it.code.code == code }
                val supportedCode = checkNotNull(result).code
                observer(supportedCode)
            }
        }
    }

    private fun resetResult(): Unit = binding.run {
        targetCodeResult.text = when {
            baseCodeInput.text.isNullOrBlank() -> getString(R.string.decimal_input_hint)
            else -> ""
        }
    }

    private fun <T> ApiResponse<T>.handle(successHandler: (T) -> Unit): Unit = when (this) {
        is NetworkResponse.Success -> successHandler(body)
        is NetworkResponse.ServerError -> {
            val message = getString(R.string.error_api)
            snackbarShort(binding.root) { message }.show()
        }
        is NetworkResponse.NetworkError -> {
            val message = getString(R.string.error_network)
            snackbarShort(binding.root) { message }.show()
        }
        is NetworkResponse.UnknownError -> {
            val message = getString(R.string.error_unknown)
            snackbarShort(binding.root) { message }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
