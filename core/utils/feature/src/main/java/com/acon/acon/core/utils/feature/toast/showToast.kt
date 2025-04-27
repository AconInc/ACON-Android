package com.acon.acon.core.utils.feature.toast

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: Int) {
    Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
}