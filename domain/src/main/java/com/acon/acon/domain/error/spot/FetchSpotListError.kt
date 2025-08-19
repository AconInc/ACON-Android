package com.acon.acon.domain.error.spot

import com.acon.acon.domain.error.RootError

open class FetchSpotListError : RootError() {

    class InvalidSpotType : FetchSpotListError() {
        override val code: Int = 40015
    }
    class InvalidCategory : FetchSpotListError() {
        override val code: Int = 40018
    }
    class InvalidOption : FetchSpotListError() {
        override val code: Int = 40019
    }
    class NonMatchingSpotTypeAndCategory : FetchSpotListError() {
        override val code: Int = 40020
    }
    class NonMatchingCategoryAndOption : FetchSpotListError() {
        override val code: Int = 40021
    }
    class OutOfServiceArea : FetchSpotListError() {
        override val code: Int = 40405
    }

    final override fun createErrorInstances(): Array<RootError> {
        return arrayOf(
            InvalidSpotType(),
            InvalidCategory(),
            InvalidOption(),
            NonMatchingSpotTypeAndCategory(),
            NonMatchingCategoryAndOption(),
            OutOfServiceArea()
        )
    }
}