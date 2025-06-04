package com.acon.acon.navigation.bottom

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.acon.acon.core.designsystem.R

enum class BottomNavType(
    @StringRes val titleRes: Int,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int
) {
    SPOT(R.string.title_spot, R.drawable.ic_starlight_filled, R.drawable.ic_starlight),
    UPLOAD(R.string.title_upload, R.drawable.ic_upload , R.drawable.ic_upload),
    PROFILE(R.string.title_profile, R.drawable.ic_profile_filled, R.drawable.ic_profile)
}