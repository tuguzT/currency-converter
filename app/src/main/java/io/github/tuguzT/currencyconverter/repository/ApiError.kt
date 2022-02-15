package io.github.tuguzT.currencyconverter.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApiError(@SerialName("error-type") val type: String)
