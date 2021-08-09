package com.ozgegn.animateddownloader

import androidx.annotation.StringRes

enum class DownLoadOptions(
    val option: Int,
    val url: String,
    @StringRes val body: Int
) {
    GLIDE(1, "https://github.com/bumptech/glide/archive/master.zip", R.string.download_glide),
    UDACITY(2, "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip", R.string.download_loadApp),
    RETROFIT(3, "https://github.com/square/retrofit/archive/master.zip", R.string.download_retrofit)
}