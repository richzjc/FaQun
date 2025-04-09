package com.wallstreetcn.global.utils

import android.text.TextUtils
import com.facebook.binaryresource.BinaryResource
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest
import java.io.File

fun getCacheFile(url: String): File? {
    if (TextUtils.isEmpty(url))
        return null
    val imageRequest = ImageRequest.fromUri(url)
    val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, imageRequest?.sourceUri)
    var resource: BinaryResource? = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
    if (resource == null) {
        resource = ImagePipelineFactory.getInstance().smallImageFileCache.getResource(cacheKey)
    }
    return if (resource == null) null else (resource as FileBinaryResource).file
}