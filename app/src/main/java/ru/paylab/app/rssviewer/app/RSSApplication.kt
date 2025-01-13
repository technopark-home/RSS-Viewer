package ru.paylab.app.rssviewer.app

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.gif.AnimatedImageDecoder
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import okio.Path.Companion.toPath

@HiltAndroidApp
class RSSApplication : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context).memoryCache {
                MemoryCache.Builder().maxSizePercent(context, percent = 0.25).build()
            }.diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.absolutePath.toPath().resolve("images_cache"))
                    .maxSizePercent(0.02).build()
            }.components {
                add(AnimatedImageDecoder.Factory())
        }.crossfade(true).build()
    }
}