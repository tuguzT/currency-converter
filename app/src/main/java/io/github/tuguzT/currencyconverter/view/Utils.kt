package io.github.tuguzT.currencyconverter.view

import kotlin.math.round

fun Double.round(decimals: Int): Double {
    var multiplier = 1
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
