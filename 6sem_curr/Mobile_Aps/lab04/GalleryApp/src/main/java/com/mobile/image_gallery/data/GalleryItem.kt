package com.mobile.image_gallery.data

import androidx.annotation.DrawableRes

data class GalleryItem(
    @DrawableRes var imageRes: Int,
    var id: Int,
    var title: String,
    var description: String,
    var rating: Float
)
