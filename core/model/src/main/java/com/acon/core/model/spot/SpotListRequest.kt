package com.acon.core.model.spot

import com.acon.core.type.CategoryType
import com.acon.core.type.FilterType
import com.acon.core.type.SpotType

data class Condition(
    val spotType: SpotType,
    val filterList: List<Filter>?,
)

data class Filter(
    val category: CategoryType,
    val optionList: List<FilterType>
)