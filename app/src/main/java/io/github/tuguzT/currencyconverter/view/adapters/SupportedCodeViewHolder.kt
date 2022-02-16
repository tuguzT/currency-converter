package io.github.tuguzT.currencyconverter.view.adapters

import androidx.recyclerview.widget.RecyclerView
import io.github.tuguzT.currencyconverter.databinding.ItemSupportedCodeBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode

class SupportedCodeViewHolder(
    private val binding: ItemSupportedCodeBinding,
    private val clickListener: (SupportedCode) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(supportedCode: SupportedCode): Unit = binding.run {
        code.text = supportedCode.code
        name.text = supportedCode.name
        root.setOnClickListener { clickListener(supportedCode) }
    }
}
