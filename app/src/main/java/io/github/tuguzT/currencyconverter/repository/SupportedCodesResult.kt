package io.github.tuguzT.currencyconverter.repository

import io.github.tuguzT.currencyconverter.model.SupportedCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SupportedCodesResult(
    @SerialName("supported_codes") val supportedCodes: Array<Array<String>>,
)

fun SupportedCodesResult.parse(): List<SupportedCode> =
    supportedCodes.map { SupportedCode(it[0], it[1]) }
