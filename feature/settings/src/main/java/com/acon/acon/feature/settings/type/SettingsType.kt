package com.acon.acon.feature.settings.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.acon.acon.feature.settings.R

enum class SettingsType(
    @DrawableRes val titleResId: Int,
    @StringRes val title: Int,
) {
    CURRENT_VERSION(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_version_info_17 ,
        title = R.string.settings_section_current_version
    ),
    TERM_OF_USE(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_term_of_use_17,
        title = R.string.settings_section_term_of_use
    ),
    PRIVACY_POLICY(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_privacy_policy_13,
        title = R.string.settings_section_private_policy
    ),
    ONBOARDING_AGAIN(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_retry_onboarding_17,
        title = R.string.settings_section_retry_onboarding
    ),
    AREA_VERIFICATION(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_area_verificaiton_g_17,
        title = R.string.settings_section_verify_area
    ),
    LOGOUT(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_logining_17,
        title = R.string.settings_section_logout
    ),
    DELETE_ACCOUNT(
        titleResId = com.acon.acon.core.designsystem.R.drawable.ic_delet_account_17,
        title = R.string.settings_section_delete_account
    ),
}