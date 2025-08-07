package com.acon.acon.core.model.type

interface FeatureType {
    fun getName(): String {
        return if (this is Enum<*>) {
            this.name
        } else {
            throw IllegalArgumentException("OptionType must be Enum")
        }
    }
}

sealed interface RestaurantFeatureType : FeatureType {

    enum class RestaurantType: FeatureType {
        KOREAN,
        CHINESE,
        JAPANESE,
        WESTERN,
        SOUTHEAST_ASIAN,
        FUSION,
        BUNSIK,
        BUFFET,
        DRINKING_PLACE,
        OTHERS;
    }
}

sealed interface CafeFeatureType : FeatureType {

    enum class CafeType: FeatureType {
        WORK_FRIENDLY,
        NOT_GOOD_FOR_WORK;
    }
}

sealed interface PriceFeatureType: FeatureType {

    enum class PriceOptionType: FeatureType {
        VALUE_FOR_MONEY,
        AVERAGE_VALUE,
        LOW_VALUE
    }
}