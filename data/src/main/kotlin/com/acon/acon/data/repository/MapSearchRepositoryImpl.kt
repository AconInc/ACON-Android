package com.acon.acon.data.repository

import com.acon.acon.core.model.model.upload.SearchedSpotByMap
import com.acon.acon.data.datasource.remote.MapSearchRemoteDataSource
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.repository.MapSearchRepository
import javax.inject.Inject

class MapSearchRepositoryImpl @Inject constructor(
    private val mapSearchRemoteDataSource: MapSearchRemoteDataSource
) : MapSearchRepository {

    override suspend fun fetchMapSearch(query: String): Result<List<SearchedSpotByMap>> {
        return runCatchingWith {
            mapSearchRemoteDataSource.fetchMapSearch(query).placeList.mapNotNull { place ->
                val foodCategory = FOOD_CATEGORIES.findLast { category ->
                    place.category.contains(category, ignoreCase = true)
                }

                foodCategory?.let {
                    SearchedSpotByMap(
                        title = place.title
                            .replace("<b>", "")
                            .replace("</b>", "")
                            .replace("\\/", "/"),
                        category = it,
                        address = place.address,
                        roadAddress = place.roadAddress
                    )
                }
            }
        }
    }

    companion object {
        private val FOOD_CATEGORIES = listOf(
            "베트남음식", "태국음식", "인도음식",
            "한식", "중식", "일식", "양식", "분식", "술집"
        )
    }
}