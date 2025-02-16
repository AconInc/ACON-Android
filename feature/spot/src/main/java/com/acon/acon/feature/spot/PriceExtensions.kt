package com.acon.acon.feature.spot

internal fun toPrice(price: Int): String {
    return if(price == -1) "변동" else "%,d원".format(price)
}