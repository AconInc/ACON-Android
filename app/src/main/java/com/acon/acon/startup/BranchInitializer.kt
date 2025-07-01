package com.acon.acon.startup

import android.content.Context
import androidx.startup.Initializer
import io.branch.referral.Branch

class BranchInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Branch.enableLogging()
        Branch.getAutoInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}