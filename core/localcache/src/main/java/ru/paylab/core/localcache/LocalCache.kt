package ru.paylab.core.localcache

import android.content.Context
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface LocalCache {

    fun getSavedIds(): Flow<Set<Int>>

    fun getSavedArticle(): Flow<Long>

    suspend fun refresh()

    suspend fun checkDocFile(id: Int)

    fun checkSavedId(id: Int): Boolean

    suspend fun saveImage(id: Int, url: String)

    fun saveDoc(id: Int): String

    fun savedDocFileName(id: Int): String

    fun savedImageFileName(id: Int): String

    fun getLocalCacheDir(): String

    fun clearSaved(id: Int)

    fun clearLocalCache()
}