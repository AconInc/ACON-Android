package com.acon.core.type

interface FilterType {
    fun getName(): String {
        return if (this is Enum<*>) {
            this.name
        } else {
            throw IllegalArgumentException("OptionType must be Enum")
        }
    }
}

sealed interface RestaurantFilterType : FilterType {

    enum class RestaurantType: FilterType {
        KOREAN,
        CHINESE,
        JAPANESE,
        WESTERN,
        ASIAN,
        FUSION,
        BUNSIK,
        BUFFET,
        DRINKING_PLACE,
        EXCLUDE_FRANCHISE;
    }

    enum class RestaurantOperationType: FilterType {
        OPEN_AFTER_10PM
    }

    enum class RestaurantPriceType: FilterType {
        VALUE_FOR_MONEY
    }
}

sealed interface CafeFilterType : FilterType {

    enum class CafeType: FilterType {
        WORK_FRIENDLY,
        EXCLUDE_FRANCHISE;
    }

    enum class CafeOperationType: FilterType {
        OPEN_AFTER_10PM
    }
}