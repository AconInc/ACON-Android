package com.acon.acon.launcher

import android.content.Context
import android.content.Intent
import com.acon.acon.MainActivity
import com.acon.acon.core.launcher.AppLauncher
import javax.inject.Inject

class AppLauncherImpl @Inject constructor(): AppLauncher {

    override fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }
}