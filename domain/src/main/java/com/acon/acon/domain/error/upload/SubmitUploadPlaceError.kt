package com.acon.acon.domain.error.upload

import com.acon.acon.domain.error.RootError

open class SubmitUploadPlaceError : RootError() {

    class InvalidSpotType : SubmitUploadPlaceError() {
        override val code: Int = 40015
    }

    class InvalidCategoryName : SubmitUploadPlaceError() {
        override val code: Int = 40016
    }

    class InvalidRestaurantFeature : SubmitUploadPlaceError() {
        override val code: Int = 40017
    }

    class InvalidCafeFeature : SubmitUploadPlaceError() {
        override val code: Int = 40018
    }

    class InvalidOpeningHour : SubmitUploadPlaceError() {
        override val code: Int = 40019
    }

    class InvalidPrice : SubmitUploadPlaceError() {
        override val code: Int = 40020
    }

    class NonMatchingSpotTypeAndCategory : SubmitUploadPlaceError() {
        override val code: Int = 40021
    }

    class NonMatchingCategoryAndOption : SubmitUploadPlaceError() {
        override val code: Int = 40022
    }

    override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidSpotType(),
            InvalidCategoryName(),
            InvalidRestaurantFeature(),
            InvalidCafeFeature(),
            InvalidOpeningHour(),
            InvalidPrice(),
            NonMatchingSpotTypeAndCategory(),
            NonMatchingCategoryAndOption()
        )
    }
}