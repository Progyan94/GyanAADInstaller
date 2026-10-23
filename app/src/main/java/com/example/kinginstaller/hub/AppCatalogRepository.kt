package com.example.kinginstaller.hub

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object AppCatalogRepository {
    private const val TAG = "AppCatalogRepository"
    private var cachedApps: List<AppModel>? = null

    fun getApps(context: Context, forceReload: Boolean = false): List<AppModel> {
        if (!forceReload && cachedApps != null) {
            return cachedApps!!
        }

        val appList = mutableListOf<AppModel>()
        try {
            val inputStream = context.assets.open("apps_catalog.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", "")
                val name = obj.optString("name", "")
                val packageName = obj.optString("packageName", "")
                val author = obj.optString("author", "")
                val category = obj.optString("category", "General")
                val accentColor = obj.optString("accentColor", "#EF4444")
                val iconUrl = obj.optString("iconUrl", "")
                val description = obj.optString("description", "")
                val githubRepo = obj.optString("githubRepo", "")
                val apkFilter = obj.optString("apkFilter", ".*\\.apk")

                val versionsList = mutableListOf<AppVersion>()
                val versionsArray = obj.optJSONArray("defaultVersions")
                if (versionsArray != null) {
                    for (j in 0 until versionsArray.length()) {
                        val vObj = versionsArray.getJSONObject(j)
                        versionsList.add(
                            AppVersion(
                                versionName = vObj.optString("versionName", ""),
                                downloadUrl = vObj.optString("downloadUrl", ""),
                                size = vObj.optString("size", ""),
                                changelog = vObj.optString("changelog", "")
                            )
                        )
                    }
                }

                val app = AppModel(
                    id = id,
                    name = name,
                    packageName = packageName,
                    author = author,
                    category = category,
                    accentColor = accentColor,
                    iconUrl = iconUrl,
                    description = description,
                    githubRepo = githubRepo,
                    apkFilter = apkFilter,
                    versions = versionsList
                )
                appList.add(app)
            }
            cachedApps = appList
        } catch (e: Exception) {
            Log.e(TAG, "Error loading apps catalog: ${e.message}", e)
        }

        return appList
    }
}
