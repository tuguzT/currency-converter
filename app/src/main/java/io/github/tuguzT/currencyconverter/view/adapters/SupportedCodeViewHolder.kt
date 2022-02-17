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
    private val stateListener: SupportedCodeViewHolder.(newState: State) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var _supportedCodeWithState: SupportedCodeWithState
    val supportedCodeWithState get() = _supportedCodeWithState

    fun bind(supportedCodeWithState: SupportedCodeWithState): Unit = binding.run {
        _supportedCodeWithState = supportedCodeWithState
        val (supportedCode, state) = _supportedCodeWithState

        code.text = supportedCode.code
        name.text = supportedCode.name
        when (state) {
            State.Saved -> showDelete()
            State.Deleted -> showSave()
        }

        root.setOnClickListener { clickListener(supportedCode) }
        saveButton.setOnClickListener {
            stateListener(this@SupportedCodeViewHolder, State.Saved)
        }
        deleteButton.setOnClickListener {
            stateListener(this@SupportedCodeViewHolder, State.Deleted)
        }
    }

    fun showSave() {
        binding.saveButton.visibility = View.VISIBLE
        binding.deleteButton.visibility = View.GONE
    }

    fun showDelete() {
        binding.saveButton.visibility = View.GONE
        binding.deleteButton.visibility = View.VISIBLE
    }
}
