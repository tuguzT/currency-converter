package io.github.tuguzT.currencyconverter.model

data class SupportedCode(val code: String, val name: String) {
    enum class State {
        Saved,
        Deleted,
    }
}
