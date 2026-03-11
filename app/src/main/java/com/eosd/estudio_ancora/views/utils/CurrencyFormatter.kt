package com.eosd.estudio_ancora.views.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency(): String {
    val brazilLocale = Locale("pt", "BR")
    val currencyFormatter = NumberFormat.getCurrencyInstance(brazilLocale)
    return currencyFormatter.format(this)
}
