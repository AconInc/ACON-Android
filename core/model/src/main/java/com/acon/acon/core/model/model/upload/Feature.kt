package com.acon.acon.core.model.model.upload

import com.acon.acon.core.model.type.CategoryType
import com.acon.acon.core.model.type.FeatureType

data class Feature(
    val category: CategoryType,
    val optionList: List<FeatureType>
)