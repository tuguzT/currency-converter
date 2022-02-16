package io.github.tuguzT.currencyconverter.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.tuguzT.currencyconverter.databinding.ItemSupportedCodeBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode

class SupportedCodesListAdapter(private val itemClickListener: (SupportedCode) -> Unit) :
    ListAdapter<SupportedCode, SupportedCodeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportedCodeViewHolder {
        val binding = ItemSupportedCodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return SupportedCodeViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: SupportedCodeViewHolder, position: Int) {
        val supportedCode = currentList[position]
        holder.bind(supportedCode)
    }

    private object DiffCallback : DiffUtil.ItemCallback<SupportedCode>() {
        override fun areItemsTheSame(old: SupportedCode, new: SupportedCode): Boolean =
            old.code == new.code

        override fun areContentsTheSame(old: SupportedCode, new: SupportedCode): Boolean =
            old == new
    }
}
