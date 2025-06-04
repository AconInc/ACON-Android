package com.acon.acon.feature.spot

import java.util.Locale

internal fun Int.toPriceString(): String = String.format(Locale.getDefault(), "%,d", this)
