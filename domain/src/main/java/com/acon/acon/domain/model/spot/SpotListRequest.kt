package com.acon.acon.domain.model.spot

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.CategoryType
import com.acon.acon.domain.type.FilterType
import com.acon.acon.domain.type.SpotType

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