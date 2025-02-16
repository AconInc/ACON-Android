package com.acon.acon.core.utils.feature.action

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.acon.acon.core.utils.feature.R

@Composable
fun BackOnPressed(
    context: Context
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= 3000L) {
            (context as? Activity)?.finish()
        } else {
            Toast.makeText(context, context.getString(R.string.toast_back_handler_close_app), Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime
        }
    }
}