package com.acon.acon.core.utils.feature.intent

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Acon 플레이스토어로 이동
 */
fun Context.launchPlayStore() {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=${packageName}")
        setPackage("com.android.vending")
    }
    startActivity(intent)
}