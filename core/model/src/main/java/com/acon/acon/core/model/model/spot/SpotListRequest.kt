package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.type.CategoryType
import com.acon.acon.core.model.type.FilterType
import com.acon.acon.core.model.type.SpotType

data class Condition(
    val spotType: SpotType,
    val filterList: List<Filter>?,
)

data class Filter(
    val category: CategoryType,
    val optionList: List<FilterType>
)