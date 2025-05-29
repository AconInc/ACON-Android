package com.acon.acon.feature.settings.type

import androidx.annotation.StringRes
import com.acon.acon.core.designsystem.R

enum class SettingsType(
    @StringRes val title: Int
) {
    TERM_OF_USE(
        title = R.string.settings_section_term_of_use
    ),
    PRIVACY_POLICY(
        title = R.string.settings_section_private_policy
    ),
    ONBOARDING_AGAIN(
        title = R.string.settings_section_retry_onboarding
    ),
    AREA_VERIFICATION(
        title = R.string.settings_section_verify_area
    ),
    LOGOUT(
        title = R.string.settings_section_logout
    ),
    DELETE_ACCOUNT(
        title = R.string.settings_section_delete_account
    )
}