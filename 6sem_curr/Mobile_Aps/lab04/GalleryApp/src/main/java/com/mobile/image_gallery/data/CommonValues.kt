package com.mobile.image_gallery.data

import com.mobile.image_gallery.R

const val PORTRAIT_GRID_COLS = 2
const val LANDSCAPE_GRID_COLS = 4
const val NO_ID = -1

object GalleryItems {
    val galleryItems = arrayListOf(
        GalleryItem(R.drawable.img1, 1, "Image 1", "Description of image nr.1", 3f),
        GalleryItem(R.drawable.img2, 2, "Image 2", "Description of image nr.2", 3f),
        GalleryItem(R.drawable.img3, 3, "Image 3", "Description of image nr.3", 3f),
        GalleryItem(R.drawable.img4, 4, "Image 4", "Description of image nr.4", 3f),
        GalleryItem(R.drawable.img5, 5, "Image 5", "Description of image nr.5", 3f),
        GalleryItem(R.drawable.img6, 6, "Image 6", "Description of image nr.6", 3f),
        GalleryItem(R.drawable.img7, 7, "Image 7", "Description of image nr.7", 3f),
        GalleryItem(R.drawable.img8, 8, "Image 8", "Description of image nr.8", 3f),
        GalleryItem(R.drawable.img9, 9, "Image 9", "Description of image nr.9", 3f),
    )
    fun sortByRating() { galleryItems.sortByDescending { it.rating } }
}