package io.github.tuguzT.currencyconverter.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.tuguzT.currencyconverter.databinding.ItemSupportedCodeBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.model.SupportedCodeWithState

class SupportedCodesListAdapter(
    private val itemClickListener: (SupportedCode) -> Unit,
    private val itemStateListener: (SupportedCodeWithState, end: () -> Unit) -> Unit,
) : ListAdapter<SupportedCodeWithState, SupportedCodeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportedCodeViewHolder {
        val binding = ItemSupportedCodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return SupportedCodeViewHolder(binding, itemClickListener, itemStateListener)
    }

    override fun onBindViewHolder(holder: SupportedCodeViewHolder, position: Int) {
        val supportedCode = currentList[position]
        holder.bind(supportedCode)
    }

    private object DiffCallback : DiffUtil.ItemCallback<SupportedCodeWithState>() {
        override fun areItemsTheSame(
            old: SupportedCodeWithState,
            new: SupportedCodeWithState,
        ): Boolean = old.code.code == new.code.code

        override fun areContentsTheSame(
            old: SupportedCodeWithState,
            new: SupportedCodeWithState,
        ): Boolean = old == new
    }
}
