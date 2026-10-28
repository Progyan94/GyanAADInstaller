package com.example.kinginstaller.hub

import java.io.Serializable

data class AppVersion(
    val versionName: String,
    val downloadUrl: String,
    val size: String = "",
    val changelog: String = ""
) : Serializable

data class AppModel(
    val id: String,
    val name: String,
    val packageName: String,
    val author: String,
    val category: String,
    val accentColor: String,
    val iconUrl: String,
    val iconDrawable: String = "",
    val description: String,
    val githubRepo: String = "",
    val apkFilter: String = ".*\\.apk",
    var versions: MutableList<AppVersion> = mutableListOf()
) : Serializable
