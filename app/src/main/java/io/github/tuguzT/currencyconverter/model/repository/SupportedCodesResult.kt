package io.github.tuguzT.currencyconverter.model.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SupportedCodesResult(
    @SerialName("supported_codes") val supportedCodes: Array<Array<String>>,
)

data class SupportedCode(val code: String, val name: String)

fun SupportedCodesResult.parse(): List<SupportedCode> =
    supportedCodes.map { SupportedCode(it[0], it[1]) }
