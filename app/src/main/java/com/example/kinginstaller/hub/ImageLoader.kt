package com.example.kinginstaller.hub

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.LruCache
import android.widget.ImageView
import com.example.kinginstaller.R
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ImageLoader {
    private val memoryCache: LruCache<String, Bitmap>
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun loadIcon(imageView: ImageView, url: String?, fallbackResId: Int = R.drawable.ic_launcher) {
        imageView.setImageResource(fallbackResId)
        if (url.isNullOrBlank()) return

        val cached = memoryCache.get(url)
        if (cached != null) {
            imageView.setImageBitmap(cached)
            return
        }

        imageView.tag = url

        thread {
            val bitmap = downloadOrLoadFromDisk(imageView.context.cacheDir, url)
            if (bitmap != null) {
                memoryCache.put(url, bitmap)
                mainHandler.post {
                    if (imageView.tag == url) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun downloadOrLoadFromDisk(cacheDir: File, urlStr: String): Bitmap? {
        val hash = urlStr.hashCode().toString()
        val diskFile = File(cacheDir, "img_$hash")

        if (diskFile.exists() && diskFile.length() > 0) {
            try {
                return BitmapFactory.decodeFile(diskFile.absolutePath)
            } catch (_: Exception) {}
        }

        try {
            val url = URL(urlStr)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 8000
                readTimeout = 8000
                instanceFollowRedirects = true
                setRequestProperty("User-Agent", "Mozilla/5.0 GyanAADHub")
            }
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val bytes = conn.inputStream.use { it.readBytes() }
                FileOutputStream(diskFile).use { it.write(bytes) }
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } catch (_: Exception) {}

        return null
    }
}
