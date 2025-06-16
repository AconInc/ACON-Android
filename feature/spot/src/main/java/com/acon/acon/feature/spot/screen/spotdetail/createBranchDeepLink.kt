package com.acon.acon.feature.spot.screen.spotdetail

import android.content.Context
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import timber.log.Timber

// TODO - 딥링크 동작 확인용 임시 코드, 수정 필요
fun createBranchDeepLink(
    context: Context,
    spotId: Long,
    spotName: String,
    imageUrl: String? = null,
    onLinkCreated: (String) -> Unit
) {
    val buo = BranchUniversalObject()
        .setCanonicalIdentifier("spot/$spotId")
        .setTitle(spotName)
        .setContentDescription("아콘에서 내 근처 $spotName 확인해보세요!")
        .apply {
            imageUrl?.let { setContentImageUrl(it) }
        }
        .setContentMetadata(
            ContentMetadata().addCustomMetadata("spotId", spotId.toString())
        )

    val lp = LinkProperties()
        .setChannel("share")
        .setFeature("spot_detail_share")
        .addControlParameter("\$android_deeplink_path", "spot/$spotId")

    buo.generateShortUrl(context, lp) { url, error ->
        if (error == null && url != null) {
            Timber.tag("Branch").d("url : $url")
            onLinkCreated(url)
        } else {
            Timber.tag("Branch").d("Branch callback: url=$url, error=${error?.message}")
            onLinkCreated(url ?: "FALLBACK")
        }
    }
}
