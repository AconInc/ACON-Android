package com.acon.acon.domain.type

sealed interface FilterType {

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

    enum class CafeType: FilterType {
        FOR_WORK,
        EXCLUDE_FRANCHISE;
    }

    enum class RestaurantOperationType: FilterType {
        AFTER_12AM
    }

    enum class CafeOperationType: FilterType {
        AFTER_10PM
    }

    enum class RestaurantPriceType: FilterType {
        COST_EFFECTIVENESS
    }
}
