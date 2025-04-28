package com.tbacademy.nextstep.presentation.extension

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tbacademy.nextstep.R

fun ImageView.loadImagesGlide(url: String, cornerRadius: Int = 0) {
    val request = Glide.with(this)
        .load(url)
        .error(R.drawable.ic_launcher_foreground)
        .placeholder(R.drawable.img_placeholder)
    if (cornerRadius > 0) {
        request.transform(RoundedCorners(cornerRadius))
    }
    request.into(this)
}

fun ImageView.loadImagesGlide(uri: Uri, cornerRadius: Int = 0) {
    val request = Glide.with(this)
        .load(uri)
        .error(R.drawable.ic_launcher_foreground)
        .placeholder(R.drawable.img_placeholder)
    if (cornerRadius > 0) {
        request.transform(RoundedCorners(cornerRadius))
    }
    request.into(this)
}

fun ImageView.loadProfilePictureGlide(url: String?) {
    val imageUrl = url ?: R.drawable.df_profile_picture

    val request = Glide.with(this)
        .load(imageUrl)
        .error(R.drawable.ic_launcher_foreground)
        .placeholder(R.drawable.img_placeholder)
    request.into(this)
}