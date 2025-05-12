package com.acon.acon.domain.type

interface FilterType

sealed interface RestaurantFilterType : FilterType {

    enum class RestaurantType: FilterType {
        KOREAN,
        CHINESE,
        JAPANESE,
        WESTERN,
        ASIAN,
        FUSION,
        KOREAN_STREET,
        BUFFET,
        DRINK_BAR;
    }

    enum class RestaurantOperationType: FilterType {
        AFTER_12AM
    }

    enum class RestaurantPriceType: FilterType {
        COST_EFFECTIVENESS
    }
}

sealed interface CafeFilterType : FilterType {

    enum class CafeType: FilterType {
        FOR_WORK,
        EXCLUDE_FRANCHISE;
    }

    enum class CafeOperationType: FilterType {
        AFTER_10PM
    }
}