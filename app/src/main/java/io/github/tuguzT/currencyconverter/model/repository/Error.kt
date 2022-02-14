package io.github.tuguzT.currencyconverter.model.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Error(@SerialName("error-type") val type: String)
