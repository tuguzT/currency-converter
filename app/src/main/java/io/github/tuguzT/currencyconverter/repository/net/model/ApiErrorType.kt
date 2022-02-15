package io.github.tuguzT.currencyconverter.repository.net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApiErrorType {
    @SerialName("unsupported-code")
    UnsupportedCode,

    @SerialName("malformed-request")
    MalformedRequest,

    @SerialName("invalid-key")
    InvalidKey,

    @SerialName("inactive-account")
    InactiveAccount,

    @SerialName("quota-reached")
    QuotaReached,
}
