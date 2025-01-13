package ru.paylab.core.localcache

import android.content.Context
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalCacheImpl @Inject constructor(
    private val context: Context
) : LocalCache {

    private val localCacheDir: String = context.filesDir.toString() + "/cache-rss"

    private val localSavedId = MutableStateFlow<Set<Int>>(emptySet())

    override fun getSavedIds(): Flow<Set<Int>> {
        return localSavedId
    }

    override fun getSavedArticle(): Flow<Long> = localSavedId.map { ids -> ids.size.toLong() }

    override suspend fun refresh() = runBlocking(Dispatchers.IO) {
        val directory = File(localCacheDir)
        val savedId = directory.listFiles { file ->
            file != null && file.extension == DOC_FILE_NAME_EXTENSION || try {
                file.nameWithoutExtension.toInt()
                true
            } catch (_: Throwable) {
                false
            }
        }?.map { file ->
            file.nameWithoutExtension.toInt()
        }?.toSet() ?: emptySet()
        localSavedId.emit(savedId)
    }

    override suspend fun checkDocFile(id: Int) {
        val path = File(localCacheDir)
        val docFile = File(path, "$id.${DOC_FILE_NAME_EXTENSION}")
        if (docFile.exists()) {
            refresh()
        }
    }

    override fun checkSavedId(id: Int): Boolean =
        localSavedId.value.contains(id)

    override suspend fun saveImage(id: Int, url: String) {
        withContext(Dispatchers.IO) {
            val img = ImageLoader(context)
            val imgLoader = ImageRequest.Builder(context).data(url).build()
            when (val resultLoading = img.execute(imgLoader)) {
                is SuccessResult -> saveBitmap(resultLoading, "$id.${IMAGE_FILE_NAME_EXTENSION}")
                is ErrorResult -> println("Load img error ${resultLoading.throwable.message}")
            }
        }
    }

    private fun saveBitmap(resImg: SuccessResult, filename: String) {
        println("Save img Succ")
        try {
            val path = File(localCacheDir)
            if (!path.exists()) path.mkdirs()

            val imageFile = File(path, filename)
            if (imageFile.exists()) {
                imageFile.delete()
            }
            imageFile.createNewFile()

            val bitmap: Bitmap = resImg.image.toBitmap()
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(
                android.graphics.Bitmap.CompressFormat.PNG,
                0 /*ignored for PNG*/,
                byteArrayOutputStream
            )
            val bitmapData = byteArrayOutputStream.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(imageFile)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            println("Save img FIN")
        } catch (e: Throwable) {
            println("Error save Image: ${e.message}")
        }
    }

    override fun saveDoc(id: Int): String {
        val filename = "$id.${DOC_FILE_NAME_EXTENSION}"
        val path = File(localCacheDir)
        if (!path.exists()) path.mkdirs()

        val docFile = File(path, filename)
        if (docFile.exists()) {
            docFile.delete()
            println("save DELETE DOC $id")
        }
        println("saveDoc: ${docFile.path}")
        return docFile.path
    }

    override fun savedDocFileName(id: Int): String {
        val filename = "$id.${DOC_FILE_NAME_EXTENSION}"
        val path = File(localCacheDir)
        if (!path.exists()) path.mkdirs()

        val docFile = File(path, filename)

        return if (docFile.exists()) {
            docFile.path
        } else ""
    }

    override fun savedImageFileName(id: Int): String {
        val filename = "$id.${IMAGE_FILE_NAME_EXTENSION}"
        val path = File(localCacheDir)
        if (!path.exists()) path.mkdirs()

        val imgFile = File(path, filename)

        return if (imgFile.exists()) {
            imgFile.path
        } else ""
    }

    override fun getLocalCacheDir(): String {
        return localCacheDir
    }

    override fun clearSaved(id: Int) {
        val path = File(localCacheDir)
        if (!path.exists()) path.mkdirs()

        val docFile = File(path, "$id.${DOC_FILE_NAME_EXTENSION}")
        if (docFile.exists()) {
            docFile.delete()
        }
        val imgFile = File(path, "$id.${IMAGE_FILE_NAME_EXTENSION}")
        if (imgFile.exists()) {
            imgFile.delete()
        }
    }

    override fun clearLocalCache() {
        val path = File(localCacheDir)
        if (path.exists()) path.deleteRecursively()
    }

    companion object {
        private const val DOC_FILE_NAME_EXTENSION: String = "mht"
        private const val IMAGE_FILE_NAME_EXTENSION: String = "png"
    }
}