package com.acon.acon.data.repository

import android.text.Html
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
            mapSearchRemoteDataSource.fetchMapSearch(query).placeList.map {
                SearchedSpotByMap(
                    title = Html.fromHtml(it.title, Html.FROM_HTML_MODE_LEGACY).toString(),
                    address = it.address
                )
            }
        }
    }
}