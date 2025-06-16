package com.acon.acon.feature.spot.screen.spotdetail

import android.content.Context
import android.widget.Toast
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import timber.log.Timber

fun createBranchDeepLink(
    context: Context,
    spotId: Long,
    spotName: String,
    onLinkCreated: (String) -> Unit
) {
    // 콘텐츠 정보 정의
    val buo = BranchUniversalObject()
        .setTitle("[Acon]$spotName")
        .setCanonicalIdentifier("spot/$spotId") // 고유 ID
        .setContentDescription("앱에서 가게 정보를 확인해보세요!")
        .setContentMetadata(
            ContentMetadata().addCustomMetadata("spotId", spotId.toString())
        )

    // 링크 속성 정의
    val lp = LinkProperties()
        .setChannel("share") // 링크 유입 경로 -> 대시보드에서 볼 수 있음
        .setFeature("spot_detail_share") // 생성된 링크의 목적/기능 -> 대시보드에서 볼 수 있음
        .addControlParameter("\$deeplink_path", "spot/$spotId") // 딥링크 클릭 시 앱의 URI Scheme으로 이동

    // 딥링크 생성
    buo.generateShortUrl(context, lp) { url, error ->
        when {
            error != null -> {
                Timber.e("Failed to create Branch link: ${error.message}")
                Toast.makeText(context, "링크 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            !url.isNullOrEmpty() -> {
                Timber.d("Branch link created: $url")
                onLinkCreated(url)
            }
            else -> {
                Timber.e("Branch returned null url")
                Toast.makeText(context, "링크 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
