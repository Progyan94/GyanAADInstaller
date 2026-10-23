package com.example.kinginstaller.hub

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kinginstaller.R
import com.google.android.material.button.MaterialButton
import java.io.File

class AppsHubAdapter(
    private val context: Context,
    private val apps: List<AppModel>,
    private val onDownloadAndInstall: (app: AppModel, version: AppVersion, onStatus: (String) -> Unit, onDone: (File) -> Unit) -> Unit
) : RecyclerView.Adapter<AppsHubAdapter.AppViewHolder>() {

    private val releasesFetchedApps = mutableSetOf<String>()

    inner class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val appAuthor: TextView = view.findViewById(R.id.appAuthor)
        val appCategory: TextView = view.findViewById(R.id.appCategory)
        val appDescription: TextView = view.findViewById(R.id.appDescription)
        val versionSpinner: Spinner = view.findViewById(R.id.versionSpinner)
        val btnDownloadInstall: MaterialButton = view.findViewById(R.id.btnDownloadInstall)
        val downloadProgressContainer: LinearLayout = view.findViewById(R.id.downloadProgressContainer)
        val downloadProgressBar: ProgressBar = view.findViewById(R.id.downloadProgressBar)
        val tvDownloadStatus: TextView = view.findViewById(R.id.tvDownloadStatus)
        val tvDownloadSpeed: TextView = view.findViewById(R.id.tvDownloadSpeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_card, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]

        holder.appName.text = app.name
        holder.appAuthor.text = "by ${app.author}"
        holder.appCategory.text = app.category
        holder.appDescription.text = app.description

        try {
            holder.appCategory.setTextColor(Color.parseColor(app.accentColor))
        } catch (_: Exception) {}

        ImageLoader.loadIcon(holder.appIcon, app.iconUrl)

        fun setupSpinner(versions: List<AppVersion>) {
            val versionLabels = versions.map { v ->
                if (v.size.isNotBlank()) "v${v.versionName} • ${v.size}" else "v${v.versionName}"
            }
            val adapter = ArrayAdapter(
                context,
                R.layout.item_version_spinner,
                versionLabels
            ).apply {
                setDropDownViewResource(R.layout.item_version_dropdown)
            }
            holder.versionSpinner.adapter = adapter
        }

        setupSpinner(app.versions)

        // Dynamically fetch releases once from GitHub if available
        if (app.githubRepo.isNotBlank() && !releasesFetchedApps.contains(app.id)) {
            releasesFetchedApps.add(app.id)
            GitHubReleaseFetcher.fetchReleases(app) { updatedVersions ->
                val currentPos = holder.bindingAdapterPosition
                if (currentPos == position) {
                    setupSpinner(updatedVersions)
                }
            }
        }

        holder.btnDownloadInstall.setOnClickListener {
            val selectedIdx = holder.versionSpinner.selectedItemPosition
            val selectedVersion = if (selectedIdx in 0 until app.versions.size) {
                app.versions[selectedIdx]
            } else if (app.versions.isNotEmpty()) {
                app.versions[0]
            } else {
                null
            }

            if (selectedVersion == null || selectedVersion.downloadUrl.isBlank()) {
                holder.tvDownloadStatus.text = "No download URL available"
                holder.downloadProgressContainer.visibility = View.VISIBLE
                return@setOnClickListener
            }

            holder.btnDownloadInstall.isEnabled = false
            holder.downloadProgressContainer.visibility = View.VISIBLE
            holder.downloadProgressBar.isIndeterminate = false
            holder.downloadProgressBar.progress = 0
            holder.tvDownloadStatus.text = "Connecting…"
            holder.tvDownloadSpeed.text = ""

            AppDownloader.downloadApk(
                context = context,
                app = app,
                version = selectedVersion,
                onProgress = { percent, speed ->
                    holder.downloadProgressBar.progress = percent
                    holder.tvDownloadStatus.text = "Downloading: $percent%"
                    holder.tvDownloadSpeed.text = speed
                },
                onSuccess = { apkFile ->
                    holder.tvDownloadStatus.text = "Download complete! Launching installer…"
                    holder.btnDownloadInstall.isEnabled = true
                    onDownloadAndInstall(app, selectedVersion, { statusMsg ->
                        holder.tvDownloadStatus.text = statusMsg
                    }, { file ->
                        // Handled
                    })
                },
                onError = { err ->
                    holder.tvDownloadStatus.text = "Error: $err"
                    holder.btnDownloadInstall.isEnabled = true
                }
            )
        }
    }

    override fun getItemCount(): Int = apps.size
}
