package io.github.tuguzT.currencyconverter.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.R
import io.github.tuguzT.currencyconverter.databinding.FragmentSupportedCodesListBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.model.SupportedCode.State
import io.github.tuguzT.currencyconverter.model.SupportedCodeWithState
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.view.adapters.SupportedCodeViewHolder
import io.github.tuguzT.currencyconverter.view.adapters.SupportedCodesListAdapter
import io.github.tuguzT.currencyconverter.view.decorations.MarginDecoration
import io.github.tuguzT.currencyconverter.viewmodel.SupportedCodesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class SupportedCodesListFragment : Fragment() {
    private val viewModel: SupportedCodesViewModel by koinNavGraphViewModel(R.id.nav_graph)
    private val args: SupportedCodesListFragmentArgs by navArgs()
    private lateinit var adapter: SupportedCodesListAdapter

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
        val itemClickListener: (SupportedCode) -> Unit = {
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(args.key, it.code)
                popBackStack()
            }
        }
        val itemStateListener: SupportedCodeViewHolder.(newState: State) -> Unit = { newState ->
            val (supportedCode, _) = supportedCodeWithState
            CoroutineScope(Dispatchers.Main).launch {
                val activity = requireActivity() as MainActivity
                activity.showProgress()

                when (newState) {
                    State.Saved -> {
                        viewModel.save(supportedCode).handle {
                            snackbarShort(binding.root) { "Code ${supportedCode.code} was saved successfully" }.show()
                            showDelete()
                        }
                        activity.hideProgress()
                    }
                    State.Deleted -> {
                        viewModel.delete(supportedCode)
                        snackbarShort(binding.root) { "Code ${supportedCode.code} was deleted successfully" }.show()
                        showSave()
                        activity.hideProgress()
                    }
                }
            }
        }

        adapter = SupportedCodesListAdapter(itemClickListener, itemStateListener)
        binding.list.adapter = adapter

        val spaceSize = resources.getDimensionPixelSize(R.dimen.item_margin)
        binding.list.addItemDecoration(MarginDecoration(spaceSize))

        binding.swipeRefresh.setOnRefreshListener(::refresh)

        lifecycleScope.launch {
            val supportedCodes = viewModel.getSupportedCodes()
            when {
                supportedCodes.isNullOrEmpty() -> refresh()
                else -> adapter.submitList(supportedCodes)
            }
            setupEmptyVisibility(viewModel.getSupportedCodes())
        }
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

    private fun setupEmptyVisibility(supportedCodes: List<SupportedCodeWithState>) {
        binding.empty.visibility = when {
            supportedCodes.isNullOrEmpty() -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true

        lifecycleScope.launch {
            viewModel.refreshSupportedCodes().handle { supportedCodes ->
                adapter.submitList(supportedCodes)
                binding.swipeRefresh.isRefreshing = false
                setupEmptyVisibility(supportedCodes)
            }
        }
    }

    private fun <T> ApiResponse<T>.handle(successHandler: (T) -> Unit): Unit = when (this) {
        is NetworkResponse.Success -> successHandler(body)
        is NetworkResponse.Error -> {
            when (this) {
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
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
