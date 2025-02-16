package com.acon.acon.domain.type

sealed interface OptionType {

    fun getName(): String {
        return if (this is Enum<*>) {
            this.name
        } else {
            throw IllegalArgumentException("OptionType must be Enum")
        }
    }

    enum class RestaurantFeatureOptionType : OptionType {
        KOREAN, WESTERN, CHINESE, JAPANESE, KOREAN_STREET, ASIAN, BAR, EXCLUDE_FRANCHISE
    }

    enum class CafeFeatureOptionType : OptionType {
        LARGE, GOOD_VIEW, DESSERT, TERRACE, EXCLUDE_FRANCHISE
    }

    enum class CompanionTypeOptionType : OptionType {
        FAMILY, DATE, FRIEND, ALONE, GROUP
    }

    enum class VisitPurposeOptionType : OptionType {
        MEETING, STUDY
    }
}