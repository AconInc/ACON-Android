package com.acon.acon.navigator

import android.content.Context
import android.content.Intent
import com.acon.acon.MainActivity
import com.acon.acon.core.navigation.navigator.AppNavigator
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(): AppNavigator {

    override fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }
}