package com.example.kinginstaller.hub

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.concurrent.thread

object AppDownloader {
    private const val TAG = "AppDownloader"
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isCancelled = false

    fun cancel() {
        isCancelled = true
    }

    fun downloadApk(
        context: Context,
        app: AppModel,
        version: AppVersion,
        onProgress: (percent: Int, speed: String) -> Unit,
        onSuccess: (apkFile: File) -> Unit,
        onError: (errorMsg: String) -> Unit
    ) {
        isCancelled = false

        thread {
            var connection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {
                val sanitizedAppName = app.name.replace("[^a-zA-Z0-9]".toRegex(), "_")
                val sanitizedVer = version.versionName.replace("[^a-zA-Z0-9]".toRegex(), "_")
                val downloadDir = File(context.filesDir, "downloads").apply { if (!exists()) mkdirs() }
                val targetFile = File(downloadDir, "${sanitizedAppName}_$sanitizedVer.apk")

                var currentUrl = version.downloadUrl
                var redirects = 0
                val maxRedirects = 6

                while (redirects < maxRedirects) {
                    val url = URL(currentUrl)
                    connection = (url.openConnection() as HttpURLConnection).apply {
                        instanceFollowRedirects = true
                        connectTimeout = 15000
                        readTimeout = 15000
                        setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile) GyanAADHub")
                    }

                    val code = connection.responseCode
                    if (code == HttpURLConnection.HTTP_MOVED_PERM ||
                        code == HttpURLConnection.HTTP_MOVED_TEMP ||
                        code == 307 || code == 308
                    ) {
                        currentUrl = connection.getHeaderField("Location")
                        redirects++
                        connection.disconnect()
                        continue
                    }
                    break
                }

                if (connection == null || connection.responseCode != HttpURLConnection.HTTP_OK) {
                    val msg = "Server responded with HTTP ${connection?.responseCode ?: -1}"
                    mainHandler.post { onError(msg) }
                    return@thread
                }

                val contentLength = connection.contentLength.toLong()
                inputStream = connection.inputStream
                outputStream = FileOutputStream(targetFile)

                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytesRead = 0L
                var lastProgressUpdate = System.currentTimeMillis()
                var lastBytesRead = 0L

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    if (isCancelled) {
                        outputStream.close()
                        targetFile.delete()
                        mainHandler.post { onError("Download cancelled") }
                        return@thread
                    }

                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead

                    val now = System.currentTimeMillis()
                    if (now - lastProgressUpdate >= 200 || totalBytesRead == contentLength) {
                        val durationSec = (now - lastProgressUpdate).coerceAtLeast(1) / 1000.0
                        val bytesSince = totalBytesRead - lastBytesRead
                        val speedBytesSec = (bytesSince / durationSec).toLong()
                        val speedText = formatSpeed(speedBytesSec)

                        val percent = if (contentLength > 0) {
                            ((totalBytesRead * 100) / contentLength).toInt().coerceIn(0, 100)
                        } else {
                            0
                        }

                        lastProgressUpdate = now
                        lastBytesRead = totalBytesRead

                        mainHandler.post {
                            onProgress(percent, speedText)
                        }
                    }
                }

                outputStream.flush()
                mainHandler.post {
                    onSuccess(targetFile)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Download error: ${e.message}", e)
                mainHandler.post {
                    onError(e.message ?: "Download failed")
                }
            } finally {
                try { inputStream?.close() } catch (_: Exception) {}
                try { outputStream?.close() } catch (_: Exception) {}
                try { connection?.disconnect() } catch (_: Exception) {}
            }
        }
    }

    private fun formatSpeed(bytesPerSec: Long): String {
        if (bytesPerSec <= 0) return ""
        val kb = bytesPerSec / 1024.0
        return if (kb < 1024) {
            String.format(Locale.US, "%.0f KB/s", kb)
        } else {
            String.format(Locale.US, "%.1f MB/s", kb / 1024.0)
        }
    }
}
