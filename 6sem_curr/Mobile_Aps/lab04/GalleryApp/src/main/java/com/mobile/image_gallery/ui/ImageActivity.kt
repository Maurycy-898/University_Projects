package com.mobile.image_gallery.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.FOCUSABLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobile.image_gallery.R
import com.mobile.image_gallery.data.GalleryItems
import com.mobile.image_gallery.data.NO_ID


class ImageActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var rating: RatingBar
    private lateinit var modeInfo: TextView

    private var currMode: Modes = Modes.VIEW_CONTENT
    private var editItemID: Int = NO_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        imageView = findViewById(R.id.imageView)
        title = findViewById(R.id.imageTitle)
        description = findViewById(R.id.imageDescription)
        modeInfo = findViewById(R.id.modeInfo)
        rating = findViewById(R.id.imgRating)

        rating.numStars = 5
        title.isEnabled = false
        description.isEnabled = false
        rating.isEnabled = false
        title.setTextColor(Color.DKGRAY)
        description.setTextColor(Color.DKGRAY)

        val hasData = intent.getBooleanExtra("hasData", false)
        if (hasData) {
            val titleText = intent.getStringExtra("title")
            val descriptionText = intent.getStringExtra("description")
            val imageRes = intent.getIntExtra("image", 0)
            val ratingStars = intent.getFloatExtra("rating",3f)
            val itemID = intent.getIntExtra("id", NO_ID)

            editItemID = itemID
            title.setText(titleText)
            description.setText(descriptionText)
            imageView.setImageResource(imageRes)
            rating.rating = ratingStars
        }
    }

    override fun onPause() {
        super.onPause()
        val item = GalleryItems.galleryItems.find { it.id == editItemID }
        if (item != null) {
            item.title = title.text.toString()
            item.description = description.text.toString()
            item.rating = rating.rating
        }
    }

    fun switchMode(view: View) {
        if (currMode == Modes.VIEW_CONTENT) {
            modeInfo.setText(R.string.hint_mode_edit)
            title.isEnabled = true
            description.isEnabled = true
            rating.isEnabled = true
            title.setTextColor(Color.BLACK)
            description.setTextColor(Color.BLACK)
            currMode = Modes.EDIT_CONTENT
        }
        else if (currMode == Modes.EDIT_CONTENT) {
            modeInfo.setText(R.string.hint_mode_view)
            title.isEnabled = false
            description.isEnabled = false
            rating.isEnabled = false
            title.setTextColor(Color.DKGRAY)
            description.setTextColor(Color.DKGRAY)
            rating.focusable = FOCUSABLE
            currMode= Modes.VIEW_CONTENT
        }
    }

    private enum class Modes { EDIT_CONTENT, VIEW_CONTENT }
}
