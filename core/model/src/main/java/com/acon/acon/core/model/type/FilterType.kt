package com.acon.acon.core.model.type

interface FilterType {
    fun getName(): String {
        return if (this is Enum<*>) {
            this.name
        } else {
            throw IllegalArgumentException("OptionType must be Enum")
        }
    }
}

sealed interface RestaurantFilterType : com.acon.acon.core.model.type.FilterType {

    enum class RestaurantType: com.acon.acon.core.model.type.FilterType {
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

    enum class RestaurantOperationType: com.acon.acon.core.model.type.FilterType {
        OPEN_AFTER_10PM
    }

    enum class RestaurantPriceType: com.acon.acon.core.model.type.FilterType {
        VALUE_FOR_MONEY
    }
}

sealed interface CafeFilterType : com.acon.acon.core.model.type.FilterType {

    enum class CafeType: com.acon.acon.core.model.type.FilterType {
        WORK_FRIENDLY,
        EXCLUDE_FRANCHISE;
    }

    enum class CafeOperationType: com.acon.acon.core.model.type.FilterType {
        OPEN_AFTER_10PM
    }
}