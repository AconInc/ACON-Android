package com.acon.core.model.spot

import androidx.compose.runtime.Immutable
import com.acon.core.type.CategoryType
import com.acon.core.type.FilterType
import com.acon.core.type.SpotType

@Immutable
data class Condition(
    val spotType: SpotType,
    val filterList: List<Filter>?,
)

@Immutable
data class Filter(
    val category: CategoryType,
    val optionList: List<FilterType>
)