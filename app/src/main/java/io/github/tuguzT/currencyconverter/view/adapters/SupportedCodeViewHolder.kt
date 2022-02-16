package io.github.tuguzT.currencyconverter.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.tuguzT.currencyconverter.databinding.ItemSupportedCodeBinding
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.model.SupportedCode.State
import io.github.tuguzT.currencyconverter.model.SupportedCodeWithState

class SupportedCodeViewHolder(
    private val binding: ItemSupportedCodeBinding,
    private val clickListener: (SupportedCode) -> Unit,
    private val stateListener: (SupportedCodeWithState) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(supportedCodeWithState: SupportedCodeWithState): Unit = binding.run {
        val (supportedCode, state) = supportedCodeWithState
        code.text = supportedCode.code
        name.text = supportedCode.name

        when (state) {
            State.Saved -> showDelete()
            State.Deleted -> showSave()
        }

        root.setOnClickListener { clickListener(supportedCode) }
        saveButton.setOnClickListener {
            stateListener(SupportedCodeWithState(supportedCode, State.Saved))
            showDelete()
        }
        deleteButton.setOnClickListener {
            stateListener(SupportedCodeWithState(supportedCode, State.Deleted))
            showSave()
        }
    }

    private fun showSave() {
        binding.saveButton.visibility = View.VISIBLE
        binding.deleteButton.visibility = View.GONE
    }

    private fun showDelete() {
        binding.saveButton.visibility = View.GONE
        binding.deleteButton.visibility = View.VISIBLE
    }
}
