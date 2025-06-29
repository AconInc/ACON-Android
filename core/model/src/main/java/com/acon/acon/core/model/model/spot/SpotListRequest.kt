package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.type.CategoryType
import com.acon.acon.core.model.type.FilterType
import com.acon.acon.core.model.type.SpotType

data class Condition(
    val spotType: com.acon.acon.core.model.type.SpotType,
    val filterList: List<com.acon.acon.core.model.model.spot.Filter>?,
)

data class Filter(
    val category: com.acon.acon.core.model.type.CategoryType,
    val optionList: List<com.acon.acon.core.model.type.FilterType>
)