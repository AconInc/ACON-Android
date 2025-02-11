package com.acon.feature.withdraw.type

import androidx.annotation.StringRes
import com.acon.feature.settings.R

enum class DeleteReasonType(
    @StringRes val reason: Int,
) {
    FEW_RESTAURANTS(reason = R.string.reason_few_restaurants),
    UNSATISFIED_RECOMMENDATIONS(reason = R.string.reason_unsatisfied_recommendations),
    FAKE_REVIEWS(reason = R.string.reason_few_restaurants),
    OTHER(reason = R.string.reason_other)
}