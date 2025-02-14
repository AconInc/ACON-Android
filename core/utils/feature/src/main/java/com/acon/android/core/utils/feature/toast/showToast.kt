package com.acon.android.core.utils.feature.toast

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: Int) {
    Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show()
}