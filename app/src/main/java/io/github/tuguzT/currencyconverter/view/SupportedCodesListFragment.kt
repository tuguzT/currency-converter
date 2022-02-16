package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.FragmentSupportedCodesListBinding
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.view.adapters.SupportedCodesListAdapter
import io.github.tuguzT.currencyconverter.view.decorations.MarginDecoration
import io.github.tuguzT.currencyconverter.viewmodel.SupportedCodesListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class SupportedCodesListFragment : Fragment() {
    companion object {
        private val LOG_TAG = SupportedCodesListFragment::class.simpleName
    }

    private val viewModel: SupportedCodesListViewModel by koinNavGraphViewModel(R.id.nav_graph)
    private lateinit var adapter: SupportedCodesListAdapter
    private val args: SupportedCodesListFragmentArgs by navArgs()

    private var _binding: FragmentSupportedCodesListBinding? = null

    // This helper property is only valid between `onCreateView` and `onDestroyView`.
    private inline val binding get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasOptionsMenu = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentSupportedCodesListBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SupportedCodesListAdapter {
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(args.key, it.code)
                popBackStack()
            }
        }
        binding.list.adapter = adapter

        val spaceSize = resources.getDimensionPixelSize(R.dimen.item_margin)
        binding.list.addItemDecoration(MarginDecoration(spaceSize))

        binding.swipeRefresh.setOnRefreshListener(::refresh)
        when {
            viewModel.supportedCodes.isNullOrEmpty() -> refresh()
            else -> adapter.submitList(viewModel.supportedCodes)
        }
        setupEmptyVisibility()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_refresh -> {
            refresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupEmptyVisibility() {
        binding.empty.visibility = when {
            viewModel.supportedCodes.isNullOrEmpty() -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true

        lifecycleScope.launch {
            viewModel.refreshSupportedCodes().handleError { supportedCodes ->
                adapter.submitList(supportedCodes)
                binding.swipeRefresh.isRefreshing = false
                setupEmptyVisibility()
            }
        }
    }

    private fun <T> ApiResponse<T>.handleError(successHandler: (T) -> Unit): Unit = when (this) {
        is NetworkResponse.Success -> successHandler(body)
        is NetworkResponse.Error -> {
            when (this) {
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
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
