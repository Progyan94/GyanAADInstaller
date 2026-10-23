package com.example.kinginstaller.hub

import android.os.Handler
import android.os.Looper
import android.util.Log
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.regex.Pattern
import kotlin.concurrent.thread

object GitHubReleaseFetcher {
    private const val TAG = "GitHubReleaseFetcher"
    private val mainHandler = Handler(Looper.getMainLooper())

    fun fetchReleases(
        app: AppModel,
        onComplete: (List<AppVersion>) -> Unit
    ) {
        if (app.githubRepo.isBlank()) {
            onComplete(app.versions)
            return
        }

        thread {
            val fetchedVersions = mutableListOf<AppVersion>()
            try {
                val apiUrl = "https://api.github.com/repos/${app.githubRepo}/releases?per_page=10"
                val url = URL(apiUrl)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 8000
                    readTimeout = 8000
                    setRequestProperty("User-Agent", "GyanAADHub-App")
                    setRequestProperty("Accept", "application/vnd.github.v3+json")
                }

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonStr = conn.inputStream.bufferedReader().use { it.readText() }
                    val releases = JSONArray(jsonStr)

                    val pattern = try {
                        Pattern.compile(app.apkFilter, Pattern.CASE_INSENSITIVE)
                    } catch (e: Exception) {
                        Pattern.compile(".*\\.apk", Pattern.CASE_INSENSITIVE)
                    }

                    for (i in 0 until releases.length()) {
                        val releaseObj = releases.getJSONObject(i)
                        val tagName = releaseObj.optString("tag_name", "").replace("^v".toRegex(), "")
                        val isPrerelease = releaseObj.optBoolean("prerelease", false)
                        val body = releaseObj.optString("body", "").trim()
                        val assets = releaseObj.optJSONArray("assets") ?: continue

                        for (j in 0 until assets.length()) {
                            val asset = assets.getJSONObject(j)
                            val assetName = asset.optString("name", "")
                            val downloadUrl = asset.optString("browser_download_url", "")
                            val sizeBytes = asset.optLong("size", 0L)

                            if (pattern.matcher(assetName).find() || (pattern.pattern() == ".*\\.apk" && assetName.endsWith(".apk", true))) {
                                val sizeStr = formatFileSize(sizeBytes)
                                val label = if (isPrerelease) "$tagName (Pre-release)" else tagName
                                fetchedVersions.add(
                                    AppVersion(
                                        versionName = label,
                                        downloadUrl = downloadUrl,
                                        size = sizeStr,
                                        changelog = body
                                    )
                                )
                                break // found primary APK for this release
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to fetch releases for ${app.name}: ${e.message}")
            }

            mainHandler.post {
                if (fetchedVersions.isNotEmpty()) {
                    app.versions = fetchedVersions
                    onComplete(fetchedVersions)
                } else {
                    onComplete(app.versions)
                }
            }
        }
    }

    private fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return ""
        val mb = bytes.toDouble() / (1024.0 * 1024.0)
        return String.format(Locale.US, "%.1f MB", mb)
    }
}
