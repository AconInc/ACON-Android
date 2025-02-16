package com.acon.acon.feature.spot.state

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Filter
import com.acon.acon.domain.type.CategoryType
import com.acon.acon.domain.type.OptionType
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.type.AvailableWalkingTimeType
import com.acon.acon.feature.spot.type.CafePriceRangeType
import com.acon.acon.feature.spot.type.RestaurantPriceRangeType

@Immutable
data class ConditionState(
    val spotType: SpotType,
    val restaurantFeatureOptionType: List<OptionType.RestaurantFeatureOptionType>,
    val companionTypeOptionType: List<OptionType.CompanionTypeOptionType>,
    val cafeFeatureOptionType: List<OptionType.CafeFeatureOptionType>,
    val visitPurposeOptionType: List<OptionType.VisitPurposeOptionType>,
    val restaurantWalkingTime: AvailableWalkingTimeType,
    val cafeWalkingTime: AvailableWalkingTimeType,
    val restaurantPriceRange: RestaurantPriceRangeType,
    val cafePriceRange: CafePriceRangeType
) {

    fun toCondition(): Condition {
        return Condition(
            spotType = spotType,
            filterList = getFilterList(),
            walkingTime = getWalkingTime(),
            priceRange = getPriceRange()
        )
    }

    private fun getPriceRange() : Int {
        return when (spotType) {
            SpotType.RESTAURANT -> restaurantPriceRange.value
            SpotType.CAFE -> cafePriceRange.value
        }
    }

    private fun getWalkingTime() : Int {
        return when (spotType) {
            SpotType.RESTAURANT -> restaurantWalkingTime.value
            SpotType.CAFE -> cafeWalkingTime.value
        }
    }

    private fun getFilterList() : List<Filter> {
        return when (spotType) {
            SpotType.RESTAURANT -> {
                listOf(
                    Filter(
                        category = CategoryType.RESTAURANT_FEATURE,
                        optionList = restaurantFeatureOptionType
                    ),
                    Filter(
                        category = CategoryType.COMPANION_TYPE,
                        optionList = companionTypeOptionType
                    ),
                )
            }

            SpotType.CAFE -> {
                listOf(
                    Filter(
                        category = CategoryType.CAFE_FEATURE,
                        optionList = cafeFeatureOptionType
                    ),
                    Filter(
                        category = CategoryType.VISIT_PURPOSE,
                        optionList = visitPurposeOptionType
                    ),
                )
            }
        }
    }
}