package com.mobile.image_gallery.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.image_gallery.*
import com.mobile.image_gallery.data.*
import com.mobile.image_gallery.ui.adapters.GalleryRecyclerAdapter
import com.mobile.image_gallery.ui.adapters.OnImageClickListener


class MainActivity : AppCompatActivity(), OnImageClickListener {
    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var recyclerAdapter: GalleryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        galleryRecyclerView = findViewById(R.id.gallery_recycler_view)

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            galleryRecyclerView.layoutManager = GridLayoutManager(this, PORTRAIT_GRID_COLS)
        } else {
            galleryRecyclerView.layoutManager = GridLayoutManager(this, LANDSCAPE_GRID_COLS)
        }
        recyclerAdapter = GalleryRecyclerAdapter(this, this)
        galleryRecyclerView.adapter = recyclerAdapter

    }

    override fun onResume() {
        super.onResume()
        GalleryItems.sortByRating()
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onImageClick(galleryItem: GalleryItem) {
        val addEventIntent = Intent(this, ImageActivity::class.java)
        addEventIntent.putExtra("hasData", true)
        addEventIntent.putExtra("id", galleryItem.id)
        addEventIntent.putExtra("title", galleryItem.title)
        addEventIntent.putExtra("description", galleryItem.description)
        addEventIntent.putExtra("rating", galleryItem.rating)
        addEventIntent.putExtra("image", galleryItem.imageRes)
        startActivity(addEventIntent)
    }
}
