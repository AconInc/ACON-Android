package com.acon.acon.core.designsystem.image

import android.content.Context
import android.graphics.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 이미지 URL을 받아 Bitmap으로 변환
 * @receiver 이미지 URL
 * @param context Context
 * @param scope CoroutineScope
 * @return Bitmap?
 */
suspend fun String.toBitmap(
    context: Context,
) : Bitmap? {

    var bitmap: Bitmap? = null
    withContext(Dispatchers.IO) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(this@toBitmap)
            .allowHardware(false)
            .build()

        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            bitmap = result.image.toBitmap()
        }
    }

    return bitmap
}