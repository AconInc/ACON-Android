package com.acon.android.domain.error.spot

import com.acon.android.domain.error.ErrorFactory
import com.acon.android.domain.error.RootError

sealed class FetchSpotListError : RootError() {

    class InvalidSpotType : FetchSpotListError() {
        override val code: Int = 40018
    }
    class InvalidCategory : FetchSpotListError() {
        override val code: Int = 40019
    }
    class InvalidOption : FetchSpotListError() {
        override val code: Int = 40020
    }
    class NonMatchingSpotTypeAndCategory : FetchSpotListError() {
        override val code: Int = 40021
    }
    class NonMatchingCategoryAndOption : FetchSpotListError() {
        override val code: Int = 40022
    }

    companion object : ErrorFactory {
        override fun createErrorInstances(): Array<RootError> {
            return arrayOf(
                InvalidSpotType(),
                InvalidCategory(),
                InvalidOption(),
                NonMatchingSpotTypeAndCategory(),
                NonMatchingCategoryAndOption()
            )
        }
    }
}