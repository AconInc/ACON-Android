package com.acon.android.domain.model.spot

import androidx.compose.runtime.Immutable
import com.acon.android.domain.type.CategoryType
import com.acon.android.domain.type.OptionType
import com.acon.android.domain.type.SpotType

@Immutable
data class Condition(
    val spotType: SpotType?,
    val filterList: List<Filter>?,
    val walkingTime: Int,
    val priceRange: Int?
) {

    companion object {
        val Default = Condition(
            spotType = null,
            filterList = null,
            walkingTime = 15,
            priceRange = null
        )
    }
}

@Immutable
data class Filter(
    val category: CategoryType,
    val optionList: List<OptionType>
)